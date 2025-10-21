package com.lextempo.calculadorapenal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lextempo.calculadorapenal.ui.viewmodel.CalculatorUiState
import com.lextempo.calculadorapenal.ui.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    state: CalculatorUiState,
    onIntent: (CalculatorViewModel.Intent) -> Unit,
    onCalculate: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                OutlinedTextField(
                    value = state.tituloCenario,
                    onValueChange = { onIntent(CalculatorViewModel.Intent.SetTitulo(it)) },
                    label = { Text("Título do cenário") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.minAnos,
                    onValueChange = { onIntent(CalculatorViewModel.Intent.SetMinAnos(it)) },
                    label = { Text("Mínimo (anos)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.minMeses,
                    onValueChange = { onIntent(CalculatorViewModel.Intent.SetMinMeses(it)) },
                    label = { Text("Mínimo (meses)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.maxAnos,
                    onValueChange = { onIntent(CalculatorViewModel.Intent.SetMaxAnos(it)) },
                    label = { Text("Máximo (anos)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                OutlinedTextField(
                    value = state.maxMeses,
                    onValueChange = { onIntent(CalculatorViewModel.Intent.SetMaxMeses(it)) },
                    label = { Text("Máximo (meses)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Reincidente: ${if (state.reincidente) "Sim" else "Não"}")
                        Button(onClick = { onIntent(CalculatorViewModel.Intent.ToggleReincidente) }) {
                            Text("Alternar")
                        }
                    }
                }
            }
            item {
                Button(
                    onClick = onCalculate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                ) {
                    Text("Calcular")
                }
            }
        }
    }
}
