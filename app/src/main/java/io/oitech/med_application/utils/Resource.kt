package io.oitech.med_application.utils

sealed class Resource<out T> {
    data class  Loading(val nothing: Nothing? =null) : Resource<Nothing>()
    data class Unspecified(val nothing: Nothing? =null) : Resource<Nothing>()
    data class Success<out T>(
        val data: T?
    ) : Resource<T>()

    data class Failure(
        val message: String
    ) : Resource<Nothing>()
}