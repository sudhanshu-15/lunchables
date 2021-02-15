package me.ssiddh.lunchables.utils

import okhttp3.Interceptor
import okhttp3.Response


private const val KEY = "key"

internal class AuthenticationInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalHttpUrl = originalRequest.url()
        val newHttpUrl = originalHttpUrl.newBuilder()
            .addQueryParameter(KEY, apiKey)
            .build()

        val newRequest = originalRequest.newBuilder().url(newHttpUrl).build()
        return chain.proceed(newRequest)
    }
}