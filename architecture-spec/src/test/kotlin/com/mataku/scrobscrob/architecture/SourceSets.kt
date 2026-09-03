package com.mataku.scrobscrob.architecture

private val productionSourceDirs = listOf("/src/main/", "/src/commonMain/", "/src/androidMain/", "/src/jvmMain/")
private val testSourceDirs = listOf("/src/test/", "/src/jvmTest/")

fun String.isProductionSourcePath(): Boolean = productionSourceDirs.any { contains(it) }

fun String.isTestSourcePath(): Boolean = testSourceDirs.any { contains(it) }
