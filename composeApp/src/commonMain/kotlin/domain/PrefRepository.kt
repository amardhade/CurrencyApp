package domain

import domain.model.CurrencyCode
import kotlinx.coroutines.flow.Flow

interface PrefRepository {

    suspend fun saveLastUpdated(lastUpdated: String)

    suspend fun isDataFresh(timpStamp: Long): Boolean

    suspend fun saveSourceCurrencyCode(code: String)
    suspend fun saveTargetCurrencyCode(code: String)
    fun readSourceCurrencyCode(): Flow<CurrencyCode>
    fun readTargetCurrencyCode(): Flow<CurrencyCode>
}