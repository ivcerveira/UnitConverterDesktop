package com.example.unitconverter.presentation

import com.example.unitconverter.domain.LinearPowerUnit
import com.example.unitconverter.domain.LogarithmicPowerUnit
import com.example.unitconverter.domain.PowerConverter
import java.math.BigDecimal
import java.math.RoundingMode

object PowerConversionText {
    fun fromLinear(text: String, from: LinearPowerUnit, to: LogarithmicPowerUnit): String =
        parse(text)?.takeIf { it > 0.0 }?.let {
            format(PowerConverter.linearToLogarithmic(it, from, to), decimalPlaces = 3)
        }.orEmpty()

    fun fromLogarithmic(text: String, from: LogarithmicPowerUnit, to: LinearPowerUnit): String =
        parse(text)?.let {
            format(PowerConverter.logarithmicToLinear(it, from, to), decimalPlaces = 4)
        }.orEmpty()

    private fun parse(text: String): Double? =
        text.trim().replace(',', '.').toDoubleOrNull()?.takeIf(Double::isFinite)

    private fun format(value: Double, decimalPlaces: Int): String = when {
        !value.isFinite() -> ""
        value == 0.0 -> "0"
        else -> BigDecimal.valueOf(value)
            .setScale(decimalPlaces, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }
}
