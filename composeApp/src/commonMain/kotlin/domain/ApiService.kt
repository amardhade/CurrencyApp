package domain

import domain.model.Currency
import domain.model.RequestState

interface ApiService {

    suspend fun fetchCurrencies(): RequestState<List<Currency>>
}