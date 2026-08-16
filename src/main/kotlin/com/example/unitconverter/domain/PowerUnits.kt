package com.example.unitconverter.domain

enum class LinearPowerUnit(
    val displayName: String,
    val wattsPerUnit: Double
) {
    PICOWATT("Picowatt (pW)", 1e-12),
    NANOWATT("Nanowatt (nW)", 1e-9),
    MICROWATT("Microwatt (µW)", 1e-6),
    MILLIWATT("Miliwatt (mW)", 1e-3),
    WATT("Watt (W)", 1.0),
    KILOWATT("Quilowatt (kW)", 1e3),
    MEGAWATT("Megawatt (MW)", 1e6)
}

enum class LogarithmicPowerUnit(val displayName: String) {
    DBM("Decibel-miliwatt (dBm)"),
    DBW("Decibel-watt (dBW)")
}
