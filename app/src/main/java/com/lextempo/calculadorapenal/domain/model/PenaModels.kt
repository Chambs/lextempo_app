package com.lextempo.calculadorapenal.domain.model

import kotlin.math.roundToInt

data class PenaBase(
    val minAnos: Int,
    val minMeses: Int = 0,
    val maxAnos: Int,
    val maxMeses: Int = 0
) {
    fun emMesesMin() = minAnos * 12 + minMeses
    fun emMesesMax() = maxAnos * 12 + maxMeses
}

data class Circunstancia(val label: String, val deltaMeses: Int)
data class CausaAumentoReducao(val label: String, val fator: Double)
data class DiasMulta(val quantidade: Int, val valorDia: Double)

data class ParametrosCalculo(
    val penaBase: PenaBase,
    val circunstancias: List<Circunstancia> = emptyList(),
    val causas: List<CausaAumentoReducao> = emptyList(),
    val diasMulta: DiasMulta? = null,
    val reincidente: Boolean = false,
    val violenciaOuGraveAmeaca: Boolean = false
)

data class ResultadoCalculo(
    val penaFinalMeses: Int,
    val penaFases: List<String>,
    val regimeInicial: RegimeInicial,
    val diasMulta: DiasMulta?,
)

enum class RegimeInicial { FECHADO, SEMIABERTO, ABERTO }

object MotorCalculoPenal {
    fun calcular(p: ParametrosCalculo): ResultadoCalculo {
        val fases = mutableListOf<String>()

        var meses = ((p.penaBase.emMesesMin() + p.penaBase.emMesesMax()) / 2.0).roundToInt()
        fases += "Pena-base (ponto médio): ${meses} meses"

        if (p.circunstancias.isNotEmpty()) {
            val delta = p.circunstancias.sumOf { it.deltaMeses }
            meses = (meses + delta).coerceAtLeast(0)
            val detalhado = p.circunstancias.joinToString { "${it.label} (${if (it.deltaMeses>=0) "+" else ""}${kotlin.math.abs(it.deltaMeses)}m)" }
            fases += "Circunstâncias: $detalhado → ${meses} meses"
        }

        if (p.causas.isNotEmpty()) {
            val fatorTotal = p.causas.fold(1.0) { acc, c -> acc * c.fator }
            val antes = meses
            meses = (meses * fatorTotal).roundToInt().coerceAtLeast(0)
            val detalhado = p.causas.joinToString { "${it.label} (x${"%.2f".format(it.fator)})" }
            fases += "Causas: $detalhado → $antes → ${meses} meses"
        }

        val regime = regimeInicialPorHeuristica(meses, p.reincidente)
        fases += "Regime sugerido: ${regime.name}"

        return ResultadoCalculo(meses, fases, regime, p.diasMulta)
    }

    private fun regimeInicialPorHeuristica(penaMeses: Int, reincidente: Boolean): RegimeInicial {
        val anos = penaMeses / 12.0
        return when {
            anos >= 8.0 -> RegimeInicial.FECHADO
            anos > 4.0 -> if (reincidente) RegimeInicial.FECHADO else RegimeInicial.SEMIABERTO
            else -> if (reincidente) RegimeInicial.SEMIABERTO else RegimeInicial.ABERTO
        }
    }
}
