package com.example.bibliotecadeseries.data.remote

import android.util.Log
import com.example.bibliotecadeseries.domain.result.AppError
import com.example.bibliotecadeseries.domain.result.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.HttpException
import java.io.IOException


suspend fun <T> safeApiCall(block: suspend () -> T): AppResult<T> =
    try {
        AppResult.Success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: HttpException) {
        AppResult.Failure(
            if (e.code() == 401) AppError.Unauthorized else AppError.Http(e.code(), e.detail())
        )
    } catch (e: IOException) {
        AppResult.Failure(AppError.Network)
    } catch (e: Exception) {
        Log.e("safeApiCall", "Error no esperado", e)
        AppResult.Failure(AppError.Unknown(e))
    }


private fun HttpException.detail(): String? = runCatching {
    response()?.errorBody()?.string()?.let {
        Json.parseToJsonElement(it).jsonObject["detail"]?.jsonPrimitive?.contentOrNull
    }
}.getOrNull()