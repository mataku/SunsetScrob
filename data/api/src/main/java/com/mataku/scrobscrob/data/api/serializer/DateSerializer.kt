package com.mataku.scrobscrob.data.api.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateSerializer : KSerializer<Date> {
  private val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US)

  override val descriptor: SerialDescriptor
    get() = PrimitiveSerialDescriptor(
      "Date",
      PrimitiveKind.STRING
    )

  override fun serialize(encoder: Encoder, value: Date) {
    encoder.encodeString(value.toString())
  }

  override fun deserialize(decoder: Decoder): Date {
    return runCatching {
      dateFormat.parse(decoder.decodeString())
    }.getOrNull() ?: Date(0)
  }
}
