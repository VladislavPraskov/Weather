package com.example.weather.utils.network

import com.example.weather.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow


/**При запросе данных, сначала грузим из кэша,
 * отображаем, потом из сети,
 * сохраняем в кэш и снова отображаем*/
abstract class NetworkBoundResource<NetworkObj, CacheObj, ResultAction>(private val isCacheNeeded: Boolean = true) {
    val result = flow() {
        // ****** STEP 1: VIEW CACHE ******

        //не лезим в базу, если не надо
        emit(
            mapToResultAction(
                cache = if (isCacheNeeded) safeCacheCall({ retrieveCache() }) else null,
                isCache = true
            )
        )

        // ****** STEP 2: MAKE NETWORK CALL, SAVE RESULT TO CACHE ******
        when (val apiResult = safeApiCall { networkRequest() }) {
            is ApiResult.Success -> {
                if (apiResult.value == null) {
                    val error = ApiResult.NetworkError(errorRes = R.string.unknown_error)
                    val resultError = mapErrorToResultAction(error)
                    emit(resultError)
                } else {
                    // ****** STEP 3: VIEW CACHE and MARK JOB COMPLETED ******
                    safeCacheCall(
                        cacheCall = { saveCache(apiResult.value) },
                        onSuccess = {
                            //не лезим в базу, если не надо
                            emit(
                                mapToResultAction(
                                    cache = if (isCacheNeeded) safeCacheCall({ retrieveCache() }) else null,
                                    isCache = false
                                )
                            )
                        }
                    )
                }
            }
            is ApiResult.NetworkError -> emit(mapErrorToResultAction(apiResult))
        }
    }

    abstract suspend fun networkRequest(): NetworkObj?
    abstract suspend fun retrieveCache(): CacheObj?
    abstract suspend fun saveCache(networkObject: NetworkObj)
    abstract fun mapToResultAction(cache: CacheObj?, isCache: Boolean): ResultAction
    abstract fun mapErrorToResultAction(error: ApiResult.NetworkError?): ResultAction

}

