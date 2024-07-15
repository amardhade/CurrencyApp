package presentation.screen.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import domain.model.RequestState
import presentation.screen.components.HomeHeader
import presentation.screens.HomeScreenViewModel
import presentation.screens.HomeUiEvent


class HomeScreen : Screen {


    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        val rateStatus = viewModel.rateStatus
        HomeHeader(
            rateStatus = rateStatus.value,
            source = RequestState.Idle,
            target = RequestState.Idle,
            amount = 20.00,
            onAmountChange = {},
            onRateRefresh = { viewModel.onEvent(HomeUiEvent.RefreshRates) },
            onSwitchClick = {},
            onCurrencyTypeSelect = {}
        )

    }
}
