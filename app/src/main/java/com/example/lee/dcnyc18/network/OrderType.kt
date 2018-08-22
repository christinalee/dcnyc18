package com.example.lee.dcnyc18.network

enum class OrderType(private val type: String) {
    Latest("latest"),
    Oldest("oldest"),
    Popular("popular");

    override fun toString(): String {
        return this.type
    }
}

