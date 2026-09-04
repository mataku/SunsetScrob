package com.mataku.scrobscrob.data.api.serializer

import com.mataku.scrobscrob.data.api.model.MultipleTag
import com.mataku.scrobscrob.data.api.model.SingleTag
import com.mataku.scrobscrob.data.api.model.TagListBody
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

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

  "tags": ""

   :memo: TODO
  "tags": {
    "tag": {
      "url": "url",
      "name": "2023"
    }
  }

 */

object TagListPolymorphicSerializer :
  JsonContentPolymorphicSerializer<TagListBody>(TagListBody::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<TagListBody> {
    return when {
      element is JsonObject -> MultipleTag.serializer()
      element is JsonPrimitive && element.isString -> SingleTag.serializer()
      else -> throw IllegalArgumentException("unknown TagList type")
    }
  }
}

object SingleTagSerializer : KSerializer<SingleTag> {
  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor("SingleTag", PrimitiveKind.STRING)

  override fun serialize(encoder: Encoder, value: SingleTag) {
    encoder.encodeString(value.name)
  }

  override fun deserialize(decoder: Decoder): SingleTag {
    return SingleTag(name = decoder.decodeString())
  }
}
