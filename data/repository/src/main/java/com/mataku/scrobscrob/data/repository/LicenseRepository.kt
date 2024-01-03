package com.mataku.scrobscrob.data.repository

import com.mataku.scrobscrob.core.entity.LicenseArtifact
import com.mataku.scrobscrob.data.db.LicenseDataStore
import com.mataku.scrobscrob.data.repository.di.LicenseInfoProvider
import com.mataku.scrobscrob.data.repository.mapper.toLicenseArtifactList
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
    cachedList = list
    emit(list)
  }.flowOn(Dispatchers.IO)
}
