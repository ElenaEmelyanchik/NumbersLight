package com.example.numberslight.utils

import android.app.Activity
import android.content.Intent
import com.google.gson.JsonElement
import com.google.gson.JsonObject

inline fun <reified T : Any> Activity.launchActivity() {
    val intent = newIntent<T>()
    startActivity(intent)
}

inline fun <reified T : Any> Activity.newIntent(): Intent =
    Intent(this, T::class.java)


fun JsonElement.getAsJsonObjectOrNull(): JsonObject? = run{
    if(this.isJsonNull)  return null
    return this.asJsonObject
}