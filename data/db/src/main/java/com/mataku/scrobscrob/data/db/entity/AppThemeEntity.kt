package com.mataku.scrobscrob.data.db.entity

enum class AppThemeEntity(val primaryId: Int) {
  DARK(1),
  LIGHT(2),
  MIDNIGHT(3),
  OCEAN(4),
  LASTFM_DARK(5);

  companion object {
    fun find(primaryId: Int?): AppThemeEntity {
      primaryId ?: return DARK
      return AppThemeEntity.entries.find {
        it.primaryId == primaryId
      } ?: DARK
    }
  }
}
