package com.example.unitconverter.presentation

import com.example.unitconverter.domain.LinearPowerUnit
import com.example.unitconverter.domain.LogarithmicPowerUnit
import kotlin.test.Test
import kotlin.test.assertEquals

class PowerConversionTextTest {
    @Test
    fun `logarithmic conversion is limited to three decimal places`() {
        assertEquals(
            "30.915",
            PowerConversionText.fromLinear(
                "1.2345",
                LinearPowerUnit.WATT,
                LogarithmicPowerUnit.DBM
            )
        )
    }

    @Test
    fun `linear conversion is limited to four decimal places`() {
        assertEquals(
            "1.2346",
            PowerConversionText.fromLogarithmic(
                "30.9151",
                LogarithmicPowerUnit.DBM,
                LinearPowerUnit.WATT
            )
        )
    }

    @Test
    fun `unnecessary trailing zeros are removed`() {
        assertEquals(
            "30",
            PowerConversionText.fromLinear(
                "1",
                LinearPowerUnit.WATT,
                LogarithmicPowerUnit.DBM
            )
        )
        assertEquals(
            "1",
            PowerConversionText.fromLogarithmic(
                "30",
                LogarithmicPowerUnit.DBM,
                LinearPowerUnit.WATT
            )
        )
    }
}
