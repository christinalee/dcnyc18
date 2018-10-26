package com.example.lee.dcnyc18.models

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.android.Main
import kotlinx.coroutines.newSingleThreadContext

class DCNYCDispatchers {
    val io = Dispatchers.IO
    val computation  = Dispatchers.Default
    val main = Dispatchers.Main
    val db = newSingleThreadContext("DB")
}

