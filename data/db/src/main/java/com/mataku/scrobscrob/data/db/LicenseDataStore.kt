package com.mataku.scrobscrob.data.db

import com.mataku.scrobscrob.data.db.entity.LicenseArtifactDefinitionEntity
import kotlinx.serialization.json.Json

object LicenseDataStore {
  private val json = Json {
    ignoreUnknownKeys = true
  }

  fun parseLicenseList(rawLicenseString: String?): List<LicenseArtifactDefinitionEntity> {
    rawLicenseString ?: return emptyList()

    return runCatching {
      json.decodeFromString<List<LicenseArtifactDefinitionEntity>>(
        rawLicenseString
      )
    }.onFailure {
      println("MATAKUDEBUG $it")
    }.getOrNull() ?: emptyList()
  }
}
