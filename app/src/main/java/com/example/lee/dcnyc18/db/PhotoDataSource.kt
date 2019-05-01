package com.example.lee.dcnyc18.db

import com.example.lee.dcnyc18.models.DCNYCDispatchers
import com.example.lee.dcnyc18.models.Photo
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.reactive.openSubscription
import kotlinx.coroutines.withContext

class PhotoDataSource(
        private val photoDao: PhotoDao,
        private val dispatchers: DCNYCDispatchers
) {
    // Streams 2: return a ReceiveChannel via openSubscription
    fun getAllPhotos(): ReceiveChannel<List<Photo>> {
        return photoDao.all.openSubscription()
    }

    // Db 2: update function signatures to use suspend and return List directly
    suspend fun insertIfNotPresent(photos: List<Photo>) {
        withContext(dispatchers.db) {
            photoDao.insertAll(photos)
        }
    }

    suspend fun persistLikeStatus(modelId: String, likeStatus: Boolean) {
        withContext(dispatchers.db) {
            photoDao.updateLikeStatus(modelId, likeStatus)
        }
    }
}