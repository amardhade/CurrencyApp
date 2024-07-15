package data

import data.local.PrefImpl
import domain.model.ApiResponse
import domain.ApiService
import domain.PrefRepository
import domain.model.Currency
import domain.model.CurrencyCode
import domain.model.RequestState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class ApiServiceImpl(private val prefRepository: PrefRepository) : ApiService {

    companion object {
        const val API_KEY = "cur_live_Za1cWaFoij854bxumQmM9Gi5xubU8IemNv9YWQrY"
        const val ENDPOINT = "https://api.currencyapi.com/v3/latest"
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 15000
        }

        install(DefaultRequest) {
            headers {
                append("apiKey", API_KEY)
            }
        }
    }

    override suspend fun fetchCurrencies(): RequestState<List<Currency>> {
        return try {
            val response = httpClient.get(ENDPOINT)
            if (response.status.value == 200) {
                val apiResponse = Json.decodeFromString<ApiResponse>(response.body())

                val availableCurrencyCodes = apiResponse.data.keys
                    .filter {
                        CurrencyCode.entries
                            .map { code -> code.name }
                            .toSet()
                            .contains(it)
                    }

                val availableCurrencies = apiResponse.data.values
                    .filter { currency ->
                        availableCurrencyCodes.contains(currency.code)
                    }


                //Persist time-stamp
                val timeStamp = apiResponse.meta.lastUpdatedAt
                prefRepository.saveLastUpdated(timeStamp)

                println("Response: ${response.body<String>()}")
                RequestState.Success(data = availableCurrencies)
            } else RequestState.Error("Http error code: ${response.status}")
        } catch (e: Exception) {
            println(e.message)
            RequestState.Error(e.message.toString())
        }
    }


}