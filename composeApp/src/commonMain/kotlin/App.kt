import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import data.di.initKoin
import org.jetbrains.compose.ui.tooling.preview.Preview
import presentation.screen.screens.HomeScreen

@Composable
@Preview
fun App() {

    initKoin()

    MaterialTheme {
        Navigator(HomeScreen())
    }
}