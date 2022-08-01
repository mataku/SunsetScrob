package com.mataku.scrobscrob.core.entity

enum class AppTheme(
    val displayName: String,
    val isLight: Boolean,
    val primaryId: Int
) {
    DARK("Dark", false, 1),
    LIGHT("Light", true, 2),
    MIDNIGHT("Midnight", false, 3);

    companion object {
        fun find(
            rawPrimaryId: Int?
        ): AppTheme {
            rawPrimaryId ?: return DARK

            return AppTheme.values().find {
                it.primaryId == rawPrimaryId
            } ?: DARK
        }
    }
}