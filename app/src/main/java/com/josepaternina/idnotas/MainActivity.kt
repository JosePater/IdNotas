package com.josepaternina.idnotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomNoteApp()
        }
    }
}

@Composable
fun RandomNoteApp() {
    // Define el array de notas ("C", "D", "E", "F", "G", "A", "B")
    val tonalidades = arrayListOf("C", "D", "G")

    // Estado para almacenar la nota actual
    var tonalidadActual by remember { mutableStateOf(tonalidades.random()) }

    // Función para cambiar la nota aleatoriamente
    fun cambiarTonalidad() {
        tonalidadActual = tonalidades.random()
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tonalidad: $tonalidadActual",
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { cambiarTonalidad() }) {
            Text(text = "Nueva tonalidad")
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun Preview() {
    RandomNoteApp()
}