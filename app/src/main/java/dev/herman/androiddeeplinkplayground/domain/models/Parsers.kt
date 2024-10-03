package dev.herman.androiddeeplinkplayground.domain.models

import android.net.Uri
import android.util.Log

fun generalParser(uri: Uri): Map<String, Any?> {
    val names = uri.queryParameterNames
    Log.d("DeeplinkPlayground", "uri: $uri")
    Log.d("DeeplinkPlayground", "host: ${uri.host}")
    Log.d("DeeplinkPlayground", "path: ${uri.path}")
    Log.d("DeeplinkPlayground", "queryParams: $names")
    return names.associateWith { n -> uri.getQueryParameter(n) }
}

fun parseUserProfile(uri: Uri): UserProfile? {
    return try {
        UserProfile(
            name = uri.getQueryParameter("name")!!,
            hobbies = uri.getQueryParameter("hobbies")!!.split(","),
            age = uri.getQueryParameter("age")!!.toInt(),
        )
    } catch (e: Exception) {
        null
    }
}
