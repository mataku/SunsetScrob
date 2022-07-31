package com.mataku.scrobscrob.core.entity

enum class AppTheme(val rawValue: String) {
    DARK("Dark"),
    LIGHT("Light");

    companion object {
        fun deserialize(
            rawValue: String?
        ): AppTheme {
            rawValue ?: return DARK

            return AppTheme.values().find {
                it.rawValue == rawValue
            } ?: DARK
        }
    }
}