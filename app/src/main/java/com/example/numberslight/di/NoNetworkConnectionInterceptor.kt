package com.example.numberslight.di

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import javax.inject.Inject

class NoNetworkConnectionInterceptor @Inject constructor(private val context: Context) :
    Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        return if (!isConnectionAvailable() || !isInternetAvailable()) {
            throw NoNetworkConnectionException()
        } else {
            chain.proceed(chain.request())
        }
    }

    private fun isConnectionAvailable(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        if (activeNetwork != null) {
            if (activeNetwork.type == ConnectivityManager.TYPE_WIFI) {
                return true
            } else if (activeNetwork.type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        } else {
           return false
        }
        return false
    }

    private fun isInternetAvailable(): Boolean {
        return try {
            Socket().apply {
                connect(InetSocketAddress("8.8.8.8", 53), 1500)
                close()
            }
            true
        } catch (e: IOException) {
            false
        }
    }

    inner class NoNetworkConnectionException: Throwable()

}