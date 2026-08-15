package com.example.unitconverter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Meu Aplicativo",
        state = rememberWindowState(width = 860.dp, height = 520.dp)
    ) {
        MaterialTheme {
            UnitConverterScreen()
        }
    }
}

@Composable
private fun UnitConverterScreen() {
    var quantity by remember { mutableStateOf("Comprimento") }
    var unit by remember { mutableStateOf("Quilômetro (km)") }
    var value by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    val quantities = listOf("Comprimento", "Massa", "Temperatura", "Volume")
    val units = listOf("Quilômetro (km)", "Metro (m)", "Centímetro (cm)")

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Text(
                text = "Conversor de Unidades",
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        LabeledDropdown(
                            label = "Grandeza",
                            value = quantity,
                            options = quantities,
                            onValueChange = { quantity = it },
                            modifier = Modifier.weight(1f)
                        )

                        LabeledTextField(
                            label = "Valor",
                            value = value,
                            onValueChange = { value = it },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        LabeledDropdown(
                            label = "Unidade",
                            value = unit,
                            options = units,
                            onValueChange = { unit = it },
                            modifier = Modifier.weight(1f)
                        )

                        LabeledTextField(
                            label = "Resultado",
                            value = result,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    // Esqueleto da tela: implemente a regra de conversão aqui.
                    result = ""
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "↻",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Converter")
            }
        }
    }
}

@Composable
private fun LabeledTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            readOnly = readOnly
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledDropdown(
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
