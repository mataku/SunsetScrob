package com.mataku.scrobscrob.data.api.serializer

import com.mataku.scrobscrob.data.api.model.TagBody
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/*
  Workaround for tags response handling.
  :memo: Tags responses that return tags in an array sometimes do not come in an array.

  expected:

  "tags": {
    "tag": [
        {
          "url": "url",
          "name": "rock"
        },
        {
          "url": "url",
          "name": "alternative"
        }
    ]
  }

  unexpected response (not always):

  "tags": {
    "tag": {
      "url": "url",
      "name": "2023"
    }
  }
 */
object TagListSerializer : KSerializer<List<TagBody>> {

  private val listSerializer = ListSerializer(TagBody.serializer())

  @OptIn(ExperimentalSerializationApi::class)
  override val descriptor: SerialDescriptor
    get() = SerialDescriptor(
      "TagsBody",
      listSerializer.descriptor
    )

  override fun deserialize(decoder: Decoder): List<TagBody> {
    return runCatching {
      decoder.decodeSerializableValue(listSerializer)
    }.getOrNull() ?: emptyList()
  }

  override fun serialize(encoder: Encoder, value: List<TagBody>) {
    encoder.encodeSerializableValue(listSerializer, value)
  }
}
