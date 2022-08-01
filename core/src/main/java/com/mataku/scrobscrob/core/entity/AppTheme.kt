package com.mataku.scrobscrob.core.entity

enum class AppTheme(
    val displayName: String,
    val isLight: Boolean,
    val primaryId: Int,
    val priority: Int
) {
    DARK("Dark", false, 1, 1),
    LIGHT("Light", true, 2, 3),
    MIDNIGHT("Midnight", false, 3, 2),
    OCEAN("Ocean", false, 4, 4),
    LASTFM_DARK("Last.fm Dark", false, 5, 5);

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