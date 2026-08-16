package com.example.unitconverter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

private enum class PowerField {
    LINEAR,
    LOGARITHMIC
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Meu Aplicativo",
        resizable = false,
        state = rememberWindowState(width = 860.dp, height = 520.dp)
    ) {
        MaterialTheme {
            UnitConverterScreen()
        }
    }
}

@Composable
private fun UnitConverterScreen() {
    var linearUnit by remember { mutableStateOf("Miliwatt (mW)") }
    var logarithmicUnit by remember { mutableStateOf("Decibel-miliwatt (dBm)") }
    var value by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val linearPowerFocusRequester = remember { FocusRequester() }
    val logarithmicPowerFocusRequester = remember { FocusRequester() }
    var focusedPowerField by remember { mutableStateOf(PowerField.LINEAR) }

    fun restoreTextFieldFocus() {
        when (focusedPowerField) {
            PowerField.LINEAR -> linearPowerFocusRequester.requestFocus()
            PowerField.LOGARITHMIC -> logarithmicPowerFocusRequester.requestFocus()
        }
    }

    //val linearUnits = listOf("pW", "nW", "uW", "mW", "W", "kW", "MW")
    val linearUnits = listOf("Picowatt (pW)", "Nanowatt (nW)", "Microwatt (µW)",
            "Miliwatt (mW)", "Watt (W)", "Quilowatt (kW)", "Megawatt (MW)")

    //val logarithmicUnits = listOf("dBm", "dBW")
    val logarithmicUnits = listOf("Decibel-miliwatt (dBm)", "Decibel-watt (dBW)")

    LaunchedEffect(Unit) {
        linearPowerFocusRequester.requestFocus()
    }

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
                            value = linearUnit,
                            options = linearUnits,
                            onValueChange = {
                                linearUnit = it
                                restoreTextFieldFocus()
                            },
                            modifier = Modifier.weight(1f)
                        )

                        LabeledTextField(
                            label = "Potência Linear",
                            value = value,
                            onValueChange = { value = it },
                            focusRequester = linearPowerFocusRequester,
                            onFocus = { focusedPowerField = PowerField.LINEAR },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        LabeledDropdown(
                            value = logarithmicUnit,
                            options = logarithmicUnits,
                            onValueChange = {
                                logarithmicUnit = it
                                restoreTextFieldFocus()
                            },
                            modifier = Modifier.weight(1f)
                        )

                        LabeledTextField(
                            label = "Potência Logarítmica",
                            value = result,
                            onValueChange = { result = it },
                            focusRequester = logarithmicPowerFocusRequester,
                            onFocus = { focusedPowerField = PowerField.LOGARITHMIC },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    value = ""
                    result = ""
                    restoreTextFieldFocus()
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .focusProperties { canFocus = false }
            ) {
                Text(
                    text = "⌫",
                    fontSize = 18.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Limpar")
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
    focusRequester: FocusRequester? = null,
    onFocus: () -> Unit = {},
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
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (focusRequester != null) Modifier.focusRequester(focusRequester)
                    else Modifier
                )
                .onFocusChanged { if (it.isFocused) onFocus() },
            singleLine = true,
            readOnly = readOnly
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LabeledDropdown(
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
            text = "Unidade",
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
                    .menuAnchor(
                        type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .focusProperties { canFocus = false }
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
                            expanded = false
                            onValueChange(option)
                        }
                    )
                }
            }
        }
    }
}
