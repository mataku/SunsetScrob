package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.core.entity.SpdxLicense
import com.mataku.scrobscrob.data.db.LicenseDataStore
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import com.mataku.scrobscrob.data.repository.mapper.toLicenseArtifactList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

interface LicenseRepository {
  fun licenseList(): Flow<List<LicenseArtifact>>
}

@Singleton
class LicenseRepositoryImpl @Inject constructor(
  private val licenseInfoProvider: LicenseInfoProvider
) : LicenseRepository {

  private var cachedList: List<LicenseArtifact> = emptyList()

  override fun licenseList(): Flow<List<LicenseArtifact>> = flow {
    if (cachedList.isNotEmpty()) {
      emit(cachedList)
      return@flow
    }

    val list = LicenseDataStore.parseLicenseList(licenseInfoProvider.licenseRawString())
      .toLicenseArtifactList()
      .sortedBy {
        it.name
      }
      .distinctBy {
        it.name
      }
      .filter {
        it.name.isNotEmpty()
      }
      .toMutableList()
    list.add(
      LicenseArtifact(
        artifactId = "",
        name = "Noto Sans",
        spdxLicenses = persistentListOf(
          SpdxLicense(
            identifier = "notosans",
            name = "Open Font License",
            url = "https://scripts.sil.org/cms/scripts/page.php?site_id=nrsi&id=OFL"
          )
        ),
        groupId = "",
        scm = null,
        version = ""
      )
    )
    cachedList = list
    emit(list)
  }.flowOn(Dispatchers.IO)
}
