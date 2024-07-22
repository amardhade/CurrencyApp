package presentation.screen.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import domain.model.CurrencyType
import presentation.components.CurrencyPickerDialog
import presentation.screen.components.HomeHeader
import presentation.screens.HomeBody
import presentation.screens.HomeScreenViewModel
import presentation.screens.HomeUiEvent
import thene.surfaceColor


class HomeScreen : Screen {


    @Composable
    override fun Content() {
        val viewModel = getScreenModel<HomeScreenViewModel>()
        val rateStatus = viewModel.rateStatus

        val sourceCurrency by viewModel.sourceCurrency
        val targetCurrency by viewModel.targetCurrency
        val allCurrencies = viewModel.allCurrencies

        var amount by rememberSaveable { mutableStateOf(0.0) }

        var selectedCurrencyType: CurrencyType by remember {
            mutableStateOf(CurrencyType.None)
        }
        var dialogOpened by remember { mutableStateOf(false) }

        if (dialogOpened && selectedCurrencyType != CurrencyType.None) {
            CurrencyPickerDialog(
                currencies = allCurrencies,
                currencyType = selectedCurrencyType,
                onConfirmClick = { currencyCode ->
                    if (selectedCurrencyType is CurrencyType.Source) {
                        viewModel.onEvent(
                            HomeUiEvent.SaveSourceCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    } else if (selectedCurrencyType is CurrencyType.Target) {
                        viewModel.onEvent(
                            HomeUiEvent.SaveTargetCurrencyCode(
                                code = currencyCode.name
                            )
                        )
                    }
                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                },
                onDismiss = {
                    selectedCurrencyType = CurrencyType.None
                    dialogOpened = false
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(surfaceColor)
        ) {
            HomeHeader(
                rateStatus = rateStatus.value,
                source = sourceCurrency,
                target = targetCurrency,
                amount = amount,
                onAmountChange = { amount = it },
                onRateRefresh = { viewModel.onEvent(HomeUiEvent.RefreshRates) },
                onSwitchClick = { viewModel.onEvent(HomeUiEvent.SwitchCurrencies) },
                onCurrencyTypeSelect = { currencyType ->
                    selectedCurrencyType = currencyType
                    dialogOpened = true
                }
            )
            HomeBody(
                source = sourceCurrency,
                target = targetCurrency,
                amount = amount
            )
        }

    }
}
