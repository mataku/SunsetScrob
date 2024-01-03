package com.mataku.scrobscrob.core.entity

data class LicenseArtifact(
  val artifactId: String,
  val groupId: String,
  val name: String,
  val scm: Scm?,
  val spdxLicenses: List<SpdxLicense>,
  val version: String,
)

data class Scm(
  val url: String,
)

data class SpdxLicense(
  val identifier: String,
  val name: String,
  val url: String,
)
