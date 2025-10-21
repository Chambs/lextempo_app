package com.lextempo.calculadorapenal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lextempo.calculadorapenal.domain.model.ResultadoCalculo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultScreen(
    result: ResultadoCalculo?,
    onNewCalc: () -> Unit
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Resultado") }) }
    ) { pad ->
        Column(
            modifier = Modifier
                .padding(pad)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (result == null) {
                Text("Nenhum resultado. Volte e calcule.")
                Button(onClick = onNewCalc) { Text("Novo cálculo") }
                return@Column
            }

            Text("Pena final: ${result.penaFinalMeses} meses")
            Text("Regime inicial sugerido: ${result.regimeInicial}")

            result.diasMulta?.let {
                HorizontalDivider()
                val total = it.quantidade * it.valorDia
                Text("Dias-multa: ${it.quantidade} × ${"R$ %.2f".format(it.valorDia)} = ${"R$ %.2f".format(total)}")
            }

            HorizontalDivider()
            Text("Etapas do cálculo:")
            result.penaFases.forEach { etapa ->
                Text("• $etapa")
            }

            Spacer(Modifier.height(16.dp))
            Button(onClick = onNewCalc) { Text("Novo cálculo") }
        }
    }
}
