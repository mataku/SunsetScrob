package com.mataku.scrobscrob.core.entity

enum class AppTheme(val rawValue: String, val isLight: Boolean) {
    DARK("Dark", false),
    LIGHT("Light", true);

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