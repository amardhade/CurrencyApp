package domain

interface PrefRepository {

    suspend fun saveLastUpdated(lastUpdated: String)

    suspend fun isDataFresh(timpStamp: Long): Boolean
}