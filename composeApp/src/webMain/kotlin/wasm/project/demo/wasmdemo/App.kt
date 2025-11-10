package wasm.project.demo.wasmdemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import wasm.project.demo.wasmdemo.ui.DashboardScreen
import wasm.project.demo.wasmdemo.ui.LoginScreen

@Composable
fun App() {
    MaterialTheme {
        var isLoggedIn by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (isLoggedIn) {
                DashboardScreen(
                    onLogout = { isLoggedIn = false }
                )
            } else {
                LoginScreen(
                    onLoginSuccess = { isLoggedIn = true }
                )
            }
        }
    }
}