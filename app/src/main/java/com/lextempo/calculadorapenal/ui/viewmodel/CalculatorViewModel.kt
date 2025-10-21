package com.lextempo.calculadorapenal.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lextempo.calculadorapenal.domain.model.*
import com.lextempo.calculadorapenal.repo.ScenariosRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

data class CalculatorUiState(
    val minAnos: String = "1",
    val minMeses: String = "0",
    val maxAnos: String = "4",
    val maxMeses: String = "0",
    val reincidente: Boolean = false,
    val circunstancias: MutableList<Circunstancia> = mutableListOf(),
    val causas: MutableList<CausaAumentoReducao> = mutableListOf(),
    val diasMultaQtd: String = "0",
    val diasMultaValor: String = "0.0",
    val tituloCenario: String = "Cálculo rápido"
)

@HiltViewModel
class CalculatorViewModel @Inject constructor(
    private val repo: ScenariosRepository
) : ViewModel() {

    var uiState by mutableStateOf(CalculatorUiState())
        private set

    var lastResult by mutableStateOf<ResultadoCalculo?>(null)
        private set

    fun onIntent(intent: Intent) {
        when (intent) {
            is Intent.SetMinAnos -> uiState = uiState.copy(minAnos = intent.v)
            is Intent.SetMinMeses -> uiState = uiState.copy(minMeses = intent.v)
            is Intent.SetMaxAnos -> uiState = uiState.copy(maxAnos = intent.v)
            is Intent.SetMaxMeses -> uiState = uiState.copy(maxMeses = intent.v)
            is Intent.ToggleReincidente -> uiState = uiState.copy(reincidente = !uiState.reincidente)
            is Intent.AddCircunstancia -> uiState.circunstancias += intent.c
            is Intent.RemoveCircunstancia -> uiState.circunstancias.remove(intent.c)
            is Intent.AddCausa -> uiState.causas += intent.c
            is Intent.RemoveCausa -> uiState.causas.remove(intent.c)
            is Intent.SetDiasMultaQtd -> uiState = uiState.copy(diasMultaQtd = intent.v)
            is Intent.SetDiasMultaValor -> uiState = uiState.copy(diasMultaValor = intent.v)
            is Intent.SetTitulo -> uiState = uiState.copy(tituloCenario = intent.v)
        }
    }

    fun calculate() {
        val penaBase = PenaBase(
            minAnos = uiState.minAnos.toIntOrNull() ?: 0,
            minMeses = uiState.minMeses.toIntOrNull() ?: 0,
            maxAnos = uiState.maxAnos.toIntOrNull() ?: 0,
            maxMeses = uiState.maxMeses.toIntOrNull() ?: 0
        )
        val dmQtd = uiState.diasMultaQtd.toIntOrNull() ?: 0
        val dmVal = uiState.diasMultaValor.toDoubleOrNull() ?: 0.0
        val dm = if (dmQtd > 0 && dmVal > 0.0) DiasMulta(dmQtd, dmVal) else null

        val params = ParametrosCalculo(
            penaBase = penaBase,
            circunstancias = uiState.circunstancias.toList(),
            causas = uiState.causas.toList(),
            diasMulta = dm,
            reincidente = uiState.reincidente
        )
        lastResult = MotorCalculoPenal.calcular(params)

        viewModelScope.launch {
            val payload = JSONObject().apply {
                put("penaBase", JSONObject().apply {
                    put("minAnos", penaBase.minAnos); put("minMeses", penaBase.minMeses)
                    put("maxAnos", penaBase.maxAnos); put("maxMeses", penaBase.maxMeses)
                })
                put("reincidente", uiState.reincidente)
                put("circunstancias", uiState.circunstancias.map { JSONObject().apply {
                    put("label", it.label); put("deltaMeses", it.deltaMeses)
                }})
                put("causas", uiState.causas.map { JSONObject().apply {
                    put("label", it.label); put("fator", it.fator)
                }})
                put("diasMulta", dm?.let { JSONObject().apply {
                    put("quantidade", it.quantidade); put("valorDia", it.valorDia)
                }})
            }.toString()
            repo.saveScenario(uiState.tituloCenario, payload)
        }
    }

    sealed interface Intent {
        data class SetMinAnos(val v: String): Intent
        data class SetMinMeses(val v: String): Intent
        data class SetMaxAnos(val v: String): Intent
        data class SetMaxMeses(val v: String): Intent
        data object ToggleReincidente: Intent
        data class AddCircunstancia(val c: Circunstancia): Intent
        data class RemoveCircunstancia(val c: Circunstancia): Intent
        data class AddCausa(val c: CausaAumentoReducao): Intent
        data class RemoveCausa(val c: CausaAumentoReducao): Intent
        data class SetDiasMultaQtd(val v: String): Intent
        data class SetDiasMultaValor(val v: String): Intent
        data class SetTitulo(val v: String): Intent
    }
}
