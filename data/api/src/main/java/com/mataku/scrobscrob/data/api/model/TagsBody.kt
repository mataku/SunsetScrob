package com.mataku.scrobscrob.data.api.model

import com.mataku.scrobscrob.data.api.serializer.SingleTagSerializer
import com.mataku.scrobscrob.data.api.serializer.TagListPolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TagBody(
  val name: String,
  val url: String
)

// last.fm album.getInfo endpoint returns weird tag response
// when multiple:
// "tags": {
//    "tag": [
//
// when single:
// "tags": {
//   "tag": ""
//
@Serializable(TagListPolymorphicSerializer::class)
sealed interface TagListBody

@Serializable(SingleTagSerializer::class)
data class SingleTag(
  val name: String
) : TagListBody

@Serializable
data class MultipleTag(
  @SerialName("tag")
  val tagList: List<TagBody>
) : TagListBody
