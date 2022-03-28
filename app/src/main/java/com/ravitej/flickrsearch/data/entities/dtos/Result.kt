package com.ravitej.flickrsearch.data.entities.dtos

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val error: String) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Initial : Result<Nothing>()
}
