package com.mataku.scrobscrob.core.entity

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

@Immutable
data class AlbumInfo(
  val albumName: String,
  val artistName: String,
  val url: String,
  val images: ImmutableList<Image>,
  val listeners: String,
  val playCount: String,
  // last.fm album.getInfo endpoint returns weird tag response
  // when multiple:
  // "tags": {
  //    "tag": [
  //
  // when single:
  // "tags": {
  //   "tag": {
  // Therefore, TODO...
  val tags: ImmutableList<Tag>,
  val tracks: ImmutableList<AlbumInfoTrack>
)

data class AlbumInfoTrack(
  val duration: String,
  val url: String,
  val name: String
)
