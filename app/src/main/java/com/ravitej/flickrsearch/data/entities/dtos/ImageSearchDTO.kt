package com.ravitej.flickrsearch.data.entities.dtos

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ImageSearchDTO(
    val title: String,
    val link: String,
    val description: String,
    val modified: String,
    val generator: String,
    val items: List<ItemsDTO>
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class ItemsDTO(
    val title: String,
    val link: String,
    val media: MediaDTO,
    val date_taken: String,
    val description: String,
    val published: String,
    val author: String,
    val author_id: String,
    val tags: String
) : Parcelable

@Parcelize
@JsonClass(generateAdapter = true)
data class MediaDTO(
    val m: String
) : Parcelable