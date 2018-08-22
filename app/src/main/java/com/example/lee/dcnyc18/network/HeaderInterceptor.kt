package com.example.lee.dcnyc18.network

import okhttp3.Interceptor
import okhttp3.Response

object HeaderInterceptor: Interceptor {
    private const val CLIENT_ID = "36c01d3dfa122a15595881bfcd6514b95cb16a890a89a049e252b7dd7940cd3c"
    override fun intercept(chain: Interceptor.Chain): Response {
        val headerRequest = chain.request().newBuilder()
                .addHeader("Accept-Version", "v1")
                .addHeader("Authorization", "Client-ID $CLIENT_ID")
                .build()
        return chain.proceed(headerRequest)
    }
}

