package data.di

import com.russhwolf.settings.Settings
import data.ApiServiceImpl
import data.local.PrefImpl
import domain.ApiService
import domain.PrefRepository
import org.koin.core.context.startKoin
import org.koin.dsl.module
import presentation.screens.HomeScreenViewModel

val appModule = module {

    single { Settings() }
    single<PrefRepository> { PrefImpl(settings = get()) }
    single<ApiService> { ApiServiceImpl(prefRepository = get())}

    factory {
        HomeScreenViewModel(
            prefRepository = get(),
            apiService = get()
        )
    }
}

fun initKoin() {
    startKoin { modules(appModule) }
}