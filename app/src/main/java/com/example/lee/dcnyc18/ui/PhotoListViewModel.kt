package com.example.lee.dcnyc18.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.NetworkOnMainThreadException
import com.example.lee.dcnyc18.db.PhotoDataSource
import com.example.lee.dcnyc18.models.DCNYCDispatchers
import com.example.lee.dcnyc18.models.Photo
import com.example.lee.dcnyc18.network.UnsplashService
import com.example.lee.dcnyc18.prefs.PrefsManager
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class PhotoListViewModel @Inject constructor( private val photoDataSource: PhotoDataSource,
        private val unsplashService: UnsplashService,
        private val prefsManager: PrefsManager,
        private val dispatchers: DCNYCDispatchers
): ViewModel(), ListIntentHandler, PhotoCellIntentHandler, CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    // TODO: this is flawed but good enough for a sample app
    private var pageToFetch = prefsManager.retrieveNextApiPage()
        set(value) {
            field = value
            prefsManager.persistNextApiPage(value)
        }

    private lateinit var photos: MutableLiveData<List<Photo>>
    // Network 6. Create a parent job for managing cancellation

    fun getPhotosListData(): LiveData<List<Photo>> {
        if (!::photos.isInitialized) {
            photos = MutableLiveData()
            listenForDataFromDb()
        }
        return photos
    }

    // Network 7. Include cancellation in lifecycle events, then update coroutines with context
    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }

    // Db 3. Update call sites
    private fun updateModelState(modelId: String, newLikeStatus: Boolean) {
        launch {
            try {
                photoDataSource.persistLikeStatus(modelId, newLikeStatus)
            } catch(e: Exception) {
               // TODO
            }
        }
    }

    // Network 4. At call site, use coroutine launcher to be able to utilize our new function
    private fun listenForDataFromDb() {
        launch {
            val channel = photoDataSource.getAllPhotos()

            for (updatedPhotos in channel) {
                if (updatedPhotos.isEmpty()) {
                    withContext(dispatchers.io) {
                        // No photos have been persisted, begin fetching from network
                        fetchNextPageOfPhotos()
                    }
                } else {
                    withContext(dispatchers.main) {
                        photos.postValue(updatedPhotos)
                    }
                }
            }
        }
    }

    // Network 3. Handle necessary logic in suspend fun
    private suspend fun fetchNextPageOfPhotos() {
        try {
            val curatedPhotos = unsplashService.getCuratedPhotos(pageToFetch++).await()
            photoDataSource.insertIfNotPresent(curatedPhotos)
        } catch (e: NetworkOnMainThreadException) {
            // Handle the exception
        }
    }

    // Network 5. Update remaining call sites
    override fun onReachedEndOfData() {
        launch(dispatchers.io) {
            fetchNextPageOfPhotos()
        }
    }

    override fun handleHeartIconClicked(photo: Photo) {
        // Toggle like status on tap
        val newLikeStatus = !photo.likedByUser
        updateModelState(photo.id, newLikeStatus)
    }

    companion object {
        private const val TAG = "YELLOW"
    }
}

