package com.example.bibliotecadeseries.domain.result

sealed interface AppError {
    data object Network : AppError
    data object Unauthorized : AppError
    data class Http(val code: Int, val detail: String?) : AppError
    data class Unknown(val cause: Throwable) : AppError
}


sealed interface AppResult<out T> {
    data class Success<out T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

inline fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (AppError) -> R
): R = when (this) {
    is AppResult.Success -> onSuccess(data)
    is AppResult.Failure -> onFailure(error)
}