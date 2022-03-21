package com.mataku.scrobscrob.core

import com.google.common.io.Files
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.nio.charset.Charset

object JsonTestHelper {
    @Throws(FileNotFoundException::class)
    private fun getAssetFile(name: String): File {
        val appDirs = arrayOf(".", "app")
        appDirs
            .map { File(it, "src/test/assets/" + name) }
            .filter { it.exists() }
            .forEach { return it }
        throw FileNotFoundException("No resource file: " + name)
    }

    @Throws(IOException::class)
    @JvmOverloads
    fun getAssetFileString(name: String, charset: Charset = Charset.defaultCharset()): String =
        Files.asCharSource(getAssetFile(name), charset).read()

}