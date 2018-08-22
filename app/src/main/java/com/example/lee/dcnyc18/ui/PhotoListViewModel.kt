package com.example.lee.dcnyc18.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.example.lee.dcnyc18.db.PhotoDataSource
import com.example.lee.dcnyc18.models.Photo
import com.example.lee.dcnyc18.network.UnsplashService
import com.example.lee.dcnyc18.prefs.PrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class PhotoListViewModel @Inject constructor(
        private val photoDataSource: PhotoDataSource,
        private val unsplashService: UnsplashService,
        private val prefsManager: PrefsManager
): ViewModel(), ListIntentHandler, PhotoCellIntentHandler {
    // TODO: this is flawed logic
    private var pageToFetch = prefsManager.retrieveNextApiPage()
        set(value) {
            field = value
            prefsManager.persistNextApiPage(value)
        }

    private lateinit var photos: MutableLiveData<List<Photo>>
    private val disposables: CompositeDisposable = CompositeDisposable()

    fun getPhotosListData(): LiveData<List<Photo>> {
        if (!::photos.isInitialized) {
            photos = MutableLiveData()
            listenForDataFromDb()
        }
        return photos
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    private fun updateModelState(modelId: String, newLikeStatus: Boolean) {
        val sub = photoDataSource.persistLikeStatus(modelId, newLikeStatus).subscribe(
                {
                    println("$TAG SUCCESS LEMUR! 🐒 ")
                },
                { _ ->
                    println("$TAG FAIL WHALE! 🐳 ")
                }
        )
        disposables.add(sub)
    }

    private fun listenForDataFromDb() {
        val sub = photoDataSource.getAllPhotos().observeOn(AndroidSchedulers.mainThread()).subscribe(
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

    private fun fetchNextPageOfPhotos() {
        val subsciption = unsplashService.getCuratedPhotos(pageToFetch = pageToFetch++)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMapCompletable {
                    photoDataSource.insertIfNotPresent(it)
                }
                .subscribe(
                        {
                            // Successfully inserted into db
                            println("Successfully inserted into db")
                        },
                        {
                            // TODO: handle the error
                            println("$TAG Error $it")
                        }
                )

        disposables.add(subsciption)
    }

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

