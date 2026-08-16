package com.example.unitconverter

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.example.unitconverter.domain.LinearPowerUnit
import com.example.unitconverter.domain.LogarithmicPowerUnit
import com.example.unitconverter.presentation.PowerConversionText

private enum class PowerField { LINEAR, LOGARITHMIC }

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Conversor de Unidades",
        resizable = false,
        state = rememberWindowState(width = 860.dp, height = 520.dp)
    ) {
        MaterialTheme { UnitConverterScreen() }
    }
}

@Composable
private fun UnitConverterScreen() {
    var linearUnit by remember { mutableStateOf(LinearPowerUnit.MILLIWATT) }
    var logarithmicUnit by remember { mutableStateOf(LogarithmicPowerUnit.DBM) }
    var linearText by remember { mutableStateOf("") }
    var logarithmicText by remember { mutableStateOf("") }
    var focusedField by remember { mutableStateOf(PowerField.LINEAR) }
    val linearFocus = remember { FocusRequester() }
    val logarithmicFocus = remember { FocusRequester() }

    fun convertFromFocusedField() {
        when (focusedField) {
            PowerField.LINEAR -> logarithmicText = PowerConversionText.fromLinear(
                linearText, linearUnit, logarithmicUnit
            )
            PowerField.LOGARITHMIC -> linearText = PowerConversionText.fromLogarithmic(
                logarithmicText, logarithmicUnit, linearUnit
            )
        }
    }

    fun restoreFocus() = when (focusedField) {
        PowerField.LINEAR -> linearFocus.requestFocus()
        PowerField.LOGARITHMIC -> logarithmicFocus.requestFocus()
    }

    LaunchedEffect(Unit) { linearFocus.requestFocus() }

    Surface(Modifier.fillMaxSize()) {
        Column(
            Modifier.fillMaxSize().padding(28.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Text("Conversor de Unidades", style = MaterialTheme.typography.headlineMedium)
            Card(
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        LabeledDropdown(
                            value = linearUnit,
                            options = LinearPowerUnit.entries,
                            label = LinearPowerUnit::displayName,
                            onValueChange = { linearUnit = it; convertFromFocusedField(); restoreFocus() },
                            modifier = Modifier.weight(1f)
                        )
                        LabeledTextField(
                            label = "Potência Linear",
                            value = linearText,
                            onValueChange = {
                                linearText = it
                                logarithmicText = PowerConversionText.fromLinear(it, linearUnit, logarithmicUnit)
                            },
                            focusRequester = linearFocus,
                            onFocus = { focusedField = PowerField.LINEAR },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        LabeledDropdown(
                            value = logarithmicUnit,
                            options = LogarithmicPowerUnit.entries,
                            label = LogarithmicPowerUnit::displayName,
                            onValueChange = { logarithmicUnit = it; convertFromFocusedField(); restoreFocus() },
                            modifier = Modifier.weight(1f)
                        )
                        LabeledTextField(
                            label = "Potência Logarítmica",
                            value = logarithmicText,
                            onValueChange = {
                                logarithmicText = it
                                linearText = PowerConversionText.fromLogarithmic(it, logarithmicUnit, linearUnit)
                            },
                            focusRequester = logarithmicFocus,
                            onFocus = { focusedField = PowerField.LOGARITHMIC },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = { linearText = ""; logarithmicText = ""; restoreFocus() },
                modifier = Modifier.align(Alignment.End).focusProperties { canFocus = false }
            ) {
                Text("⌫", fontSize = 18.sp)
                Spacer(Modifier.width(8.dp))
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
    focusRequester: FocusRequester,
    onFocus: () -> Unit
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth().focusRequester(focusRequester)
                .onFocusChanged { if (it.isFocused) onFocus() },
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun <T> LabeledDropdown(
    value: T,
    options: List<T>,
    label: (T) -> String,
    onValueChange: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text("Unidade", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = label(value),
                onValueChange = {},
                modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                    .focusProperties { canFocus = false }.fillMaxWidth(),
                readOnly = true,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) }
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(label(option)) },
                        onClick = { expanded = false; onValueChange(option) }
                    )
                }
            }
        }
    }
}
