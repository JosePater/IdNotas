package com.josepaternina.idnotas

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AllTonalidades() {
    Column {
        Text(text = "Todas las tonalidades", style = MaterialTheme.typography.titleMedium)
    }
}