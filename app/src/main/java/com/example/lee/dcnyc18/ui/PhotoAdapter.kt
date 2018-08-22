package com.example.lee.dcnyc18.ui

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.lee.dcnyc18.R
import com.example.lee.dcnyc18.models.Photo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Single

class PhotoAdapter(
        private val photoCellIntentHandler: PhotoCellIntentHandler
): RecyclerView.Adapter<PhotoViewHolder>() {
    private lateinit var disposables: CompositeDisposable
    private var data: List<Photo> = listOf()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        disposables = CompositeDisposable()
    }

    fun updateData(updatedPhotos: List<Photo>) {
        Single.create<DiffUtil.DiffResult> {
            val diffCallback = PhotoModelDiffCallback(this.data, updatedPhotos)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            it.onSuccess(diffResult)
        }.subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { diffResult ->
                            data = updatedPhotos
                            diffResult.dispatchUpdatesTo(this@PhotoAdapter)
                        }
                )
                .also { disposables.add(it) }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        disposables.dispose()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val photoView = LayoutInflater.from(parent.context).inflate(R.layout.view_photo_cell, parent, false)
        return PhotoViewHolder(photoView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photo = data[position]
        holder.bindData(photo, photoCellIntentHandler)
    }

    companion object {
        private const val TAG = "YELLOW"
    }
}


