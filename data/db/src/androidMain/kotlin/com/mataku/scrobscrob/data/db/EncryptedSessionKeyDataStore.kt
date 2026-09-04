package com.mataku.scrobscrob.data.db

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import androidx.datastore.tink.AeadSerializer
import com.google.crypto.tink.Aead
import com.google.crypto.tink.KeyTemplate
import com.google.crypto.tink.KeysetHandle
import com.google.crypto.tink.RegistryConfiguration
import com.google.crypto.tink.aead.AeadConfig
import com.google.crypto.tink.aead.PredefinedAeadParameters
import com.google.crypto.tink.integration.android.AndroidKeysetManager
import java.io.IOException
import java.security.GeneralSecurityException

private const val DATA_FILE_NAME = "session_key.pb"
private const val KEYSET_NAME = "session_key_keyset"
private const val KEYSET_PREF_FILE = "session_key_keyset_prefs"
private const val MASTER_KEY_URI = "android-keystore://session_key_master_key"

internal fun encryptedSessionKeyDataStore(context: Context): DataStore<String> {
  AeadConfig.register()

  val keysetHandle = try {
    buildKeysetHandle(context)
  } catch (e: GeneralSecurityException) {
    resetEncryptedState(context)
    buildKeysetHandle(context)
  } catch (e: IOException) {
    resetEncryptedState(context)
    buildKeysetHandle(context)
  }

  val aead = keysetHandle.getPrimitive(RegistryConfiguration.get(), Aead::class.java)
  val serializer = AeadSerializer(
    aead = aead,
    wrappedSerializer = SessionKeySerializer,
    associatedData = DATA_FILE_NAME.encodeToByteArray(),
  )

  return DataStoreFactory.create(
    serializer = serializer,
    produceFile = { context.dataStoreFile(DATA_FILE_NAME) },
  )
}

private fun buildKeysetHandle(context: Context): KeysetHandle =
  AndroidKeysetManager.Builder()
    .withSharedPref(context, KEYSET_NAME, KEYSET_PREF_FILE)
    .withKeyTemplate(KeyTemplate.createFrom(PredefinedAeadParameters.AES256_GCM))
    .withMasterKeyUri(MASTER_KEY_URI)
    .build()
    .keysetHandle

private fun resetEncryptedState(context: Context) {
  context.getSharedPreferences(KEYSET_PREF_FILE, Context.MODE_PRIVATE)
    .edit()
    .clear()
    .commit()
  context.dataStoreFile(DATA_FILE_NAME).takeIf { it.exists() }?.delete()
}
