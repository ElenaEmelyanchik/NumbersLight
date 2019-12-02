package com.example.numberslight.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.google.gson.JsonElement
import com.google.gson.JsonObject

/*inline fun <reified T : Any> Activity.launchActivity() {
    val intent = newIntent<T>()
    startActivity(intent)
}*/

inline fun <reified T : Activity> launch(
    context: Context,
    init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(context).apply {
        init()
    }
    context.startActivity(intent)
}

inline fun <reified T : Activity> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


fun JsonElement.getAsJsonObjectOrNull(): JsonObject? = run{
    if(this.isJsonNull)  return null
    return this.asJsonObject
}