package com.example.lee.dcnyc18.network

import com.example.lee.dcnyc18.models.Photo
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashService {

    // Network 2. Change return type of Retrofit call
    @GET("/photos/curated")
    fun getCuratedPhotos(
            @Query("page") pageToFetch: Int = 1,
            @Query("per_page") photosPerPage: Int = 10,
            @Query("order_by") orderBy: OrderType = OrderType.Latest
    ) : Deferred<List<Photo>>

}
