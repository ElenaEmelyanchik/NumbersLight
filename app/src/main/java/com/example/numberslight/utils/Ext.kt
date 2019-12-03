package com.example.numberslight.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.view.View
import com.example.numberslight.R
import com.google.android.material.snackbar.Snackbar
import com.google.gson.JsonElement
import com.google.gson.JsonObject
object Ext {
    @JvmStatic
    fun handleNoNetworkError(view: View, action: () -> Unit, resources: Resources): Snackbar? = view.handleNoNetworkError(action, resources)
}
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


    fun JsonElement.getAsJsonObjectOrNull(): JsonObject? = run {
        if (this.isJsonNull) return null
        return this.asJsonObject
    }

    fun View.handleNoNetworkError(action: () -> Unit, resources: Resources): Snackbar? {
        return Snackbar.make(
            this,
            resources.getText(R.string.network_error),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(resources.getText(R.string.network_retry)) {
                action.invoke()
            }
            .setActionTextColor(resources.getColor(android.R.color.holo_red_light))
            .apply {
                show()
            }
    }



