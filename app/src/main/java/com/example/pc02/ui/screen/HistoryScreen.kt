package com.example.pc02.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc02.ui.viewmodel.ConvertViewModel

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    val vm: ConvertViewModel = viewModel()
    val history by vm.history.collectAsState()
    LaunchedEffect(Unit) { vm.loadHistory() }

    Column(Modifier.padding(16.dp)) {
        Button(onClick = onBack) { Text("Volver") }
        Spacer(Modifier.height(8.dp))
        @Composable
        fun HistoryScreen(onBack: () -> Unit) {
            val vm: ConvertViewModel = viewModel()
            val history by vm.history.collectAsState()
            LaunchedEffect(Unit) { vm.loadHistory() }

            Column(Modifier.padding(16.dp)) {
                Button(onClick = onBack) { Text("Volver") }
                Spacer(Modifier.height(8.dp))
                // **Añade esto** para ver cuántos items trae:
                Text("Items en historial: ${history.size}")
                Spacer(Modifier.height(8.dp))

                LazyColumn {
                    items(history) { c -> /* tu Card… */ }
                }
            }
        }
        LazyColumn {
            items(history) { c ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text("${c.amount} ${c.from} → ${"%.2f".format(c.result)} ${c.to}")
                        Text("Hora: ${c.timestamp.toDate()}")
                    }
                }
            }
        }
    }
}