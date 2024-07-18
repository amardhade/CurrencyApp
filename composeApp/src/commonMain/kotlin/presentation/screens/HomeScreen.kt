package presentation.screen.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import presentation.screen.components.HomeHeader
import presentation.screens.HomeScreenViewModel
import presentation.screens.HomeUiEvent


class HomeScreen : Screen {


    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        val rateStatus = viewModel.rateStatus

        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency

        var amount by rememberSaveable { mutableStateOf(0.0) }

        HomeHeader(
            rateStatus = rateStatus.value,
            source = sourceCurrency,
            target = targetCurrency,
            amount = amount,
            onAmountChange = { amount = it },
            onRateRefresh = { viewModel.onEvent(HomeUiEvent.RefreshRates) },
            onSwitchClick = { viewModel.onEvent(HomeUiEvent.SwitchCurrencies) },
            onCurrencyTypeSelect = {}
        )

    }
}
