package com.ravitej.flickrsearch.data.repository

import com.ravitej.flickrsearch.data.entities.dtos.ImageSearchDTO
import com.ravitej.flickrsearch.data.entities.dtos.Result
import com.ravitej.flickrsearch.data.remote.FlickrSearchService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FlickrSearchRepository @Inject constructor(
    private val imageSearchService: FlickrSearchService
) {

    suspend fun getImageResults(imageQuery: String): Flow<Result<ImageSearchDTO>> {
        return flow {
            emit(Result.Loading)
            val result = imageSearchService.fetchImageResults(searchQuery = imageQuery)
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(Result.Success(it))
                } ?: emit(Result.Error("No results found..."))
            } else {
                emit(Result.Error("Network call failed..."))
            }
        }.flowOn(Dispatchers.IO)
    }
}