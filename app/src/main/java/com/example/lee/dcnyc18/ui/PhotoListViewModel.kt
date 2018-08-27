package com.example.lee.dcnyc18.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.lee.dcnyc18.db.PhotoDataSource
import com.example.lee.dcnyc18.models.DCNYCDispatchers
import com.example.lee.dcnyc18.models.Photo
import com.example.lee.dcnyc18.network.UnsplashService
import com.example.lee.dcnyc18.prefs.PrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.experimental.Job
import javax.inject.Inject

class PhotoListViewModel @Inject constructor( private val photoDataSource: PhotoDataSource,
        private val unsplashService: UnsplashService,
        private val prefsManager: PrefsManager,
        private val dispatchers: DCNYCDispatchers
): ViewModel(), ListIntentHandler, PhotoCellIntentHandler {

    // TODO: this is flawed but good enough for a sample app
    private var pageToFetch = prefsManager.retrieveNextApiPage()
        set(value) {
            field = value
            prefsManager.persistNextApiPage(value)
        }

    private lateinit var photos: MutableLiveData<List<Photo>>
    private val disposables: CompositeDisposable = CompositeDisposable()
    // Network 6. Create a parent job for managing cancellation
//    private val compositeJob = Job()

    fun getPhotosListData(): LiveData<List<Photo>> {
        if (!::photos.isInitialized) {
            photos = MutableLiveData()
            listenForDataFromDb()
        }
        return photos
    }

    // Network 7. Include cancellation in lifecycle events, then update coroutines with context
    override fun onCleared() {
        disposables.dispose()
//        compositeJob.cancel()
        super.onCleared()
    }

    // Db 3. Update call sites
    private fun updateModelState(modelId: String, newLikeStatus: Boolean) {
        val sub = photoDataSource.persistLikeStatus(modelId, newLikeStatus)
                .subscribe(
                        {
                            println("$TAG SUCCESS LEMUR! 🐒 ")
                        },
                        { _ ->
                            println("$TAG FAIL WHALE! 🐳 ")
                        }
                )
        disposables.add(sub)
    }

    // Network 4. At call site, use coroutine launcher to be able to utilize our new function
    private fun listenForDataFromDb() {
        // Streams 3: Handle channel iteration
        val sub = photoDataSource.getAllPhotos()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { updatedPhotos ->
                            if (updatedPhotos.isEmpty()) {
                                // No photos have been persisted, begin fetching from network
                                fetchNextPageOfPhotos()
                            } else {
                                photos.postValue(updatedPhotos)
                            }
                        },
                        {
                            println("$TAG Error fetching photos from db $it")
                        }
                )
        disposables.add(sub)
    }

    // Network 3. Handle necessary logic in suspend fun
    private fun fetchNextPageOfPhotos() {
        val subscription = unsplashService.getCuratedPhotos(pageToFetch = pageToFetch++)
                .subscribeOn(Schedulers.io())
                .flatMapCompletable {
                    photoDataSource.insertIfNotPresent(it)
                }
                .subscribe(
                        {
                            println("$TAG Successfully inserted into db")
                        },
                        {
                            // TODO: handle the error
                            println("$TAG Womp womp $it")
                        }
                )

        disposables.add(subscription)
    }

    // Network 5. Update remaining call sites
    override fun onReachedEndOfData() {
        fetchNextPageOfPhotos()
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

