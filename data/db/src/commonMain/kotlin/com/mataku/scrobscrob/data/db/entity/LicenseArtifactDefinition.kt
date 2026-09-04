package com.mataku.scrobscrob.data.db.entity

import kotlinx.serialization.Serializable

@Serializable
data class LicenseArtifactDefinitionEntity(
  val artifactId: String,
  val groupId: String,
  val name: String = "",
  val scm: ScmEntity? = null,
  val spdxLicenses: List<SpdxLicenseEntity> = emptyList(),
  val version: String,
)

@Serializable
data class ScmEntity(
  val url: String,
)

@Serializable
data class SpdxLicenseEntity(
  val identifier: String,
  val name: String,
  val url: String,
)
