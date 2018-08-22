package com.example.lee.dcnyc18.ui

import android.graphics.Color
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.lee.dcnyc18.R
import com.example.lee.dcnyc18.models.Photo
import com.squareup.picasso.Picasso

class PhotoViewHolder(private val view: View): RecyclerView.ViewHolder(view) {
    private val imageView: AppCompatImageView = view.findViewById(R.id.view_photo_cell_image_view)
    private val heartIcon: View = view.findViewById(R.id.view_photo_cell_heart_icon)
    private var loadedUrl: String? = null

    fun bindData(photo: Photo, photoIconIntentHandler: PhotoCellIntentHandler) {
        val urlToLoad = photo.urls.regular
        if (urlToLoad != loadedUrl) {
            Picasso.get().load(urlToLoad).noPlaceholder().into(imageView)
            loadedUrl = urlToLoad
        }

        view.setBackgroundColor((Math.random()*Color.BLACK).toInt())

        val heartDrawable = if (photo.likedByUser) R.drawable.heart_selected else R.drawable.heart_unselected
        heartIcon.setBackgroundResource(heartDrawable)

        heartIcon.setOnClickListener {
            photoIconIntentHandler.handleHeartIconClicked(photo)
        }
    }
}

