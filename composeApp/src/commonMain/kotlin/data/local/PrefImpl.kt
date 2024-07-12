package data.local

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import domain.PrefRepository
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalSettingsApi::class)
class PrefImpl(private val settings: Settings) : PrefRepository {

    companion object {
        const val TIMESTAMP = "time_stamp"
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
}