package com.example.lee.dcnyc18.db

import com.example.lee.dcnyc18.models.DCNYCDispatchers
import com.example.lee.dcnyc18.models.Photo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class PhotoDataSource(
        private val photoDao: PhotoDao,
        private val dispatchers: DCNYCDispatchers
) {
    // Streams 2: return a ReceiveChannel via openSubscription
    fun getAllPhotos(): Flowable<List<Photo>> {
        return photoDao.all.subscribeOn(Schedulers.io())
    }

    // Db 2: update function signatures to use suspend and return List directly
    fun insertIfNotPresent(photos: List<Photo>): Completable {
        return Completable.fromCallable { photoDao.insertAll(photos) }.subscribeOn(Schedulers.io())
    }

    fun persistLikeStatus(modelId: String, likeStatus: Boolean): Completable {
        return Completable.fromCallable {
            photoDao.updateLikeStatus(modelId, likeStatus)
        }.subscribeOn(Schedulers.io())
    }
}