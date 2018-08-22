package com.example.lee.dcnyc18.ui

import com.example.lee.dcnyc18.models.Photo

interface PhotoCellIntentHandler {
    fun handleHeartIconClicked(photo: Photo)
}

interface ListIntentHandler {
    fun onReachedEndOfData()
}
