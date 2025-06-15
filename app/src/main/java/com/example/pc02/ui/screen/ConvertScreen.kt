package com.example.pc02.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pc02.ui.viewmodel.ConvertViewModel

@Composable
fun ConvertScreen(
    onLogout: () -> Unit,
    onShowHistory: () -> Unit
) {
    val vm: ConvertViewModel = viewModel()
    val rates by vm.rates.collectAsState()
    var amountText by remember { mutableStateOf("100") }
    var from by remember { mutableStateOf(rates.keys.firstOrNull() ?: "USD") }
    var to   by remember { mutableStateOf(rates.keys.drop(1).firstOrNull() ?: "EUR") }
    var result by remember { mutableStateOf<Double?>(null) }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Monto") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Text("De:")
        DropdownMenuSpinner(options = rates.keys.toList(), selected = from) { from = it }
        Spacer(Modifier.height(8.dp))
        Text("A:")
        DropdownMenuSpinner(options = rates.keys.toList(), selected = to) { to = it }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                val amt = amountText.toDoubleOrNull() ?: 0.0
                vm.convert(amt, from, to) { res -> result = res }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Convertir")
        }
        Spacer(Modifier.height(16.dp))
        result?.let {
            Text("$amountText $from equivale a ${"%.2f".format(it)} $to")
        }
        Spacer(Modifier.height(24.dp))
        Row {
            Button(onClick = onShowHistory, Modifier.weight(1f)) { Text("Historial") }
            Spacer(Modifier.width(8.dp))
            Button(onClick = onLogout, Modifier.weight(1f))       { Text("Cerrar sesión") }
        }
    }
}