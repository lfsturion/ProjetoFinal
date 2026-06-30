package com.example.financeapp.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = koinViewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .padding(16.dp)
    ) {

        Text("Configurações")

        Button(onClick = {
            viewModel.logout {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }) {
            Text("Sair")
        }
    }
}