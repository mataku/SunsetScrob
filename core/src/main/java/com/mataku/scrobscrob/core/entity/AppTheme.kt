package com.mataku.scrobscrob.core.entity

enum class AppTheme(val rawValue: String) {
    DARK("dark"),
    LIGHT("light");

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