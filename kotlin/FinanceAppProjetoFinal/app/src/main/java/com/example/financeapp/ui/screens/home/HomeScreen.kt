package com.example.financeapp.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.financeapp.domain.model.Transaction
import com.example.financeapp.domain.model.TransactionType
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onOpenSettings: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTransaction by remember { mutableStateOf<Transaction?>(null) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Text("+")
            }
        }
    ) { padding ->

        if (showDeleteDialog && selectedTransaction != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                confirmButton = {
                    Button(onClick = {
                        val deleted = selectedTransaction!!

                        viewModel.deleteTransaction(deleted)
                        showDeleteDialog = false

                        scope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Transação excluída",
                                actionLabel = "Desfazer",
                                duration = SnackbarDuration.Short
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                viewModel.addTransaction(
                                    deleted.description,
                                    deleted.amount,
                                    deleted.date,
                                    deleted.type == TransactionType.INCOME
                                )
                            }
                        }
                    }) {
                        Text("Excluir")
                    }
                },
                dismissButton = {
                    Button(onClick = { showDeleteDialog = false }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Excluir transação") },
                text = { Text("Tem certeza que deseja excluir?") }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = {
                    viewModel.logout {
                        onOpenSettings()
                    }
                }
            ) {
                Text("Sair")
            }

            SummarySection(state)

            Spacer(modifier = Modifier.height(16.dp))

            TransactionList(
                state = state,
                onEdit = {
                    selectedTransaction = it
                    showBottomSheet = true
                },
                onDelete = {
                    selectedTransaction = it
                    showDeleteDialog = true
                }
            )
        }

        if (showBottomSheet) {
            TransactionBottomSheet(
                transaction = selectedTransaction,
                onDismiss = {
                    showBottomSheet = false
                    selectedTransaction = null
                },
                onSave = { description, value, date, isIncome ->

                    if (selectedTransaction == null) {
                        viewModel.addTransaction(description, value, date, isIncome)
                    } else {
                        viewModel.updateTransaction(
                            selectedTransaction!!.copy(
                                description = description,
                                amount = value,
                                date = date,
                                type = if (isIncome)
                                    TransactionType.INCOME
                                else
                                    TransactionType.EXPENSE
                            )
                        )
                    }

                    showBottomSheet = false
                    selectedTransaction = null
                }
            )
        }

        if (state.loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        state.error?.let {
            LaunchedEffect(it) {
                snackbarHostState.showSnackbar(it)
            }
        }
    }
}