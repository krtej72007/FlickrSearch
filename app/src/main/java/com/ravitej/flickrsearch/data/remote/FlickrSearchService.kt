package com.ravitej.flickrsearch.data.remote

import com.ravitej.flickrsearch.data.entities.dtos.ImageSearchDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrSearchService {

    @GET("services/feeds/photos_public.gne")
    suspend fun fetchImageResults(
        @Query("format") format: String = "json",
        @Query("nojsoncallback") nojsoncallback: Int = 1,
        @Query("searchQuery") searchQuery: String
    ): Response<ImageSearchDTO>
}