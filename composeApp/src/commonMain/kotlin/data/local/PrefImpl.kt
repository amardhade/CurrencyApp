package data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.PrefRepository
import domain.model.CurrencyCode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalSettingsApi::class)
class PrefImpl(private val settings: Settings) : PrefRepository {

    companion object {
        const val TIMESTAMP = "time_stamp"
        const val SOURCE_CURRENCY_KEY = "source_currency_key"
        const val TARGET_CURRENCY_KEY = "target_currency_key"

        val DEFAULT_SOURCE_CURRENCY = CurrencyCode.USD.name
        val DEFAULT_TARGET_CURRENCY = CurrencyCode.EUR.name
    }

    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    override suspend fun saveLastUpdated(lastUpdated: String) {
        flowSettings.putLong(TIMESTAMP, Instant.parse(lastUpdated).toEpochMilliseconds())
    }

    override suspend fun isDataFresh(currentTimpStamp: Long): Boolean {
        val savedTimeStamp = flowSettings.getLong(TIMESTAMP, 0L)

        return if (savedTimeStamp != 0L) {
            val currentTimeStamp = Instant.fromEpochMilliseconds(currentTimpStamp)
            val savedTimeStamp = Instant.fromEpochMilliseconds(savedTimeStamp)
            val currentDateTime = currentTimeStamp
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val savedDateTime = savedTimeStamp
                .toLocalDateTime(TimeZone.currentSystemDefault())
            val daysDifference = currentDateTime.date.dayOfYear - savedDateTime.date.dayOfYear
            daysDifference < 1
        } else false
    }

    override suspend fun saveSourceCurrencyCode(code: String) {
        flowSettings.putString(SOURCE_CURRENCY_KEY, code)
    }

    override suspend fun saveTargetCurrencyCode(code: String) {
        flowSettings.putString(SOURCE_CURRENCY_KEY, code)
    }

    override fun readSourceCurrencyCode(): Flow<CurrencyCode> {
        return flowSettings.getStringFlow(SOURCE_CURRENCY_KEY, DEFAULT_SOURCE_CURRENCY)
            .map { CurrencyCode.valueOf(it) }
    }

    override fun readTargetCurrencyCode(): Flow<CurrencyCode> {
        return flowSettings.getStringFlow(TARGET_CURRENCY_KEY, DEFAULT_TARGET_CURRENCY)
            .map { CurrencyCode.valueOf(it) }
    }
}