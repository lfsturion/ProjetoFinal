package com.example.financeapp.ui.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit
) {
    val state = viewModel.state

    if (state.logged) {
        onLoginSuccess()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = {
                Text("Senha")
            },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { viewModel.login() },
            enabled = !state.loading
        ) {
            if (state.loading) {
                CircularProgressIndicator()
            } else {
                Text("Entrar")
            }
        }

        if (state.loading) {
            Text("Carregando...")
        }

        state.error?.let {
            Text(it, color = Color.Red)
        }
    }
}