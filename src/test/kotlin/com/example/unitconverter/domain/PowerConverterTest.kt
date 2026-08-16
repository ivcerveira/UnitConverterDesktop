package com.example.unitconverter.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PowerConverterTest {
    @Test
    fun `one milliwatt is zero dBm`() {
        assertEquals(0.0, PowerConverter.linearToLogarithmic(1.0, LinearPowerUnit.MILLIWATT, LogarithmicPowerUnit.DBM), 1e-12)
    }

    @Test
    fun `one watt is zero dBW and thirty dBm`() {
        assertEquals(0.0, PowerConverter.linearToLogarithmic(1.0, LinearPowerUnit.WATT, LogarithmicPowerUnit.DBW), 1e-12)
        assertEquals(30.0, PowerConverter.linearToLogarithmic(1.0, LinearPowerUnit.WATT, LogarithmicPowerUnit.DBM), 1e-12)
    }

    @Test
    fun `thirty dBm is one watt`() {
        assertEquals(1.0, PowerConverter.logarithmicToLinear(30.0, LogarithmicPowerUnit.DBM, LinearPowerUnit.WATT), 1e-12)
    }

    @Test
    fun `all linear scales round trip`() {
        LinearPowerUnit.entries.forEach { unit ->
            val logarithmic = PowerConverter.linearToLogarithmic(12.345, unit, LogarithmicPowerUnit.DBM)
            assertEquals(12.345, PowerConverter.logarithmicToLinear(logarithmic, LogarithmicPowerUnit.DBM, unit), 1e-9)
        }
    }

    @Test
    fun `zero linear power is rejected`() {
        assertFailsWith<IllegalArgumentException> {
            PowerConverter.linearToLogarithmic(0.0, LinearPowerUnit.WATT, LogarithmicPowerUnit.DBW)
        }
    }
}
