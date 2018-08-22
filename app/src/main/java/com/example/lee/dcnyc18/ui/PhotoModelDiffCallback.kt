package com.example.lee.dcnyc18.ui

import android.support.v7.util.DiffUtil
import com.example.lee.dcnyc18.models.Photo


class PhotoModelDiffCallback(
        private val oldList: List<Photo>,
        private val newList: List<Photo>
) : DiffUtil.Callback() {
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].likedByUser == newList[oldItemPosition].likedByUser
    }
}

