package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class LicenseArtifact(
  val artifactId: String,
  val groupId: String,
  val name: String,
  val scm: Scm?,
  val spdxLicenses: ImmutableList<SpdxLicense>,
  val version: String,
)

@Immutable
data class Scm(
  val url: String,
)

@Immutable
data class SpdxLicense(
  val identifier: String,
  val name: String,
  val url: String,
)
