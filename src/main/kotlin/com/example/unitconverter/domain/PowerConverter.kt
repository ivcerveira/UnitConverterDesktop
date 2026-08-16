package com.example.unitconverter.domain

import kotlin.math.log10
import kotlin.math.pow

/** Pure conversion logic, independent of Compose and reusable by other clients. */
object PowerConverter {
    fun linearToLogarithmic(
        value: Double,
        from: LinearPowerUnit,
        to: LogarithmicPowerUnit
    ): Double {
        require(value > 0.0 && value.isFinite()) { "Linear power must be positive and finite." }

        val watts = value * from.wattsPerUnit
        val dbw = 10.0 * log10(watts)
        return when (to) {
            LogarithmicPowerUnit.DBW -> dbw
            LogarithmicPowerUnit.DBM -> dbw + 30.0
        }
    }

    fun logarithmicToLinear(
        value: Double,
        from: LogarithmicPowerUnit,
        to: LinearPowerUnit
    ): Double {
        require(value.isFinite()) { "Logarithmic power must be finite." }

        val dbw = when (from) {
            LogarithmicPowerUnit.DBW -> value
            LogarithmicPowerUnit.DBM -> value - 30.0
        }
        val watts = 10.0.pow(dbw / 10.0)
        return watts / to.wattsPerUnit
    }
}
