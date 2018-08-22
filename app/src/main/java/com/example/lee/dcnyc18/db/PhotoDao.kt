package com.example.lee.dcnyc18.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.example.lee.dcnyc18.models.PHOTO_TABLE
import com.example.lee.dcnyc18.models.Photo
import io.reactivex.Flowable

@Dao
interface PhotoDao {
    @get:Query("SELECT * FROM $PHOTO_TABLE")
    val all: Flowable<List<Photo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(photos: List<Photo>): LongArray

    @Query("UPDATE $PHOTO_TABLE SET likedByUser = :likeStatus  WHERE id = :modelId")
    fun updateLikeStatus(modelId: String, likeStatus: Boolean)
}