package com.example.lee.dcnyc18.models

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.newSingleThreadContext
import kotlinx.coroutines.experimental.rx2.asCoroutineDispatcher

class DCNYCDispatchers {
    val io = Schedulers.io().asCoroutineDispatcher()
    val computation  = Schedulers.computation().asCoroutineDispatcher()
    val main = AndroidSchedulers.mainThread().asCoroutineDispatcher()
    val db = newSingleThreadContext("DB")
}

