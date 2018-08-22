package com.example.lee.dcnyc18.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import android.arch.persistence.room.TypeConverters

const val PHOTO_TABLE = "photos"
@Entity(tableName = PHOTO_TABLE,
        indices = arrayOf(Index("id"))
)
@TypeConverters(PhotoConverter::class)
data class Photo(
        @PrimaryKey var id: String,
        var width: Int,
        var height: Int,
        var color: String, // TODO: hex
        var urls: Urls,
        var likedByUser: Boolean = false
)

object PhotoConverter {
        @TypeConverter
        @JvmStatic
        fun urlsToString(urls: Urls?): String? {
                urls ?: return null

                // LOLSOB. Don't do this
                return "${urls.regular} ${urls.full} ${urls.raw} ${urls.small} ${urls.thumb}"
        }

        @TypeConverter
        @JvmStatic
        fun stringToUrls(str: String?): Urls? {
                str ?: return null

                val components = str.split(" ")
                val regular = components[0]
                val full = components[1]
                val raw = components[2]
                val small = components[3]
                val thumb = components[4]
                return Urls(regular = regular, full = full, raw = raw, small = small, thumb = thumb)
        }
}
