package com.example.lee.dcnyc18.db

import com.example.lee.dcnyc18.models.Photo
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class PhotoDataSource(
        private val photoDao: PhotoDao
) {
    fun getAllPhotos(): Flowable<List<Photo>> {
        return photoDao.all.subscribeOn(Schedulers.io())
    }

    fun insertIfNotPresent(photos: List<Photo>): Completable {
        return Completable.fromCallable { photoDao.insertAll(photos) }.subscribeOn(Schedulers.io())
    }

    fun persistLikeStatus(modelId: String, likeStatus: Boolean): Completable {
        return Completable.fromCallable {
            photoDao.updateLikeStatus(modelId, likeStatus)
        }.subscribeOn(Schedulers.io())
    }
}