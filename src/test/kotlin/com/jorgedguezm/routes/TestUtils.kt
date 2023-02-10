package com.jorgedguezm.routes

import okio.buffer
import okio.source
import java.nio.charset.StandardCharsets

fun ClassLoader.getStringJson(path: String): String {
    val inputStream = getResourceAsStream("api/$path")
    val source = inputStream?.let { inputStream.source().buffer() }
    return source!!.readString(StandardCharsets.UTF_8)
}

fun String.getId() = split(":")[2].split(",")[0]