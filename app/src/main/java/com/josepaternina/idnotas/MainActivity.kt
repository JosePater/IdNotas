package com.josepaternina.idnotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.text.style.TextAlign

// Tonalidades ("C", "D", "E", "F", "G", "A", "B")
val tonalidades = listOf("C", "D", "G")
val num = listOf(1, 2, 3, 4, 5, 6)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(1.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tonalidad: ${GlobalStateTonal.tonalidadActual}",
            fontSize = 22.sp
        )
        Text(
            text = "# ${GlobalStateTonal.numActual}",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
        NoteButtonsRow()
    }
}

// Singleton object que almacena la tonalidad actual
object GlobalStateTonal {
    // variable global reactiva
    var tonalidadActual by mutableStateOf(tonalidades.random())
    var numActual by mutableIntStateOf(num.random())

    // Función para cambiar la tonalidad aleatoriamente
    fun cambiarTonalidad() {
        tonalidadActual = tonalidades.random()
        numActual = num.random()
    }
}

// Botones
@Composable
fun NoteButtonsRow() {
    // Define el array de nombres de botones
    val notasMayores = arrayListOf("C", "D", "E", "F", "G", "A", "B")
    val notasMenores = arrayListOf("Cm", "Dm", "Em", "Fm", "Gm", "Am", "Bm")
    val notasMenoresSos = arrayListOf("C#m", "D#m", "F#m", "G#m", "A#m")

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Notas Mayores
        Row {
            notasMayores.forEach { nota ->
                Button(
                    onClick = {
                        GlobalStateTonal.cambiarTonalidad()
                    },
                    modifier = Modifier
                        .width(54.dp) // Establece el ancho del botón
                        .height(54.dp) // Establece la altura del botón
                        .padding(2.dp) // Aplica el margen interno del botón
                ) {
                    Text(
                        text = nota,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center // Centra el texto horizontalmente
                    )
                }
            }
        }

        //Notas menores
        Row {
            notasMenores.forEach { nota ->
                FilledTonalButton(
                    onClick = {
                        GlobalStateTonal.cambiarTonalidad()
                    },
                    modifier = Modifier
                        .width(56.dp) // Establece el ancho del botón
                        .height(56.dp) // Establece la altura del botón
                        .padding(2.dp) // Aplica el margen interno del botón
                ) {
                    Text(
                        text = nota,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Notas menores #
        Row(
            horizontalArrangement = Arrangement.Center // Espacia uniformemente los botones
        ) {
            notasMenoresSos.forEach { nota ->
                OutlinedButton(
                    onClick = {
                        GlobalStateTonal.cambiarTonalidad()
                    },
                    modifier = Modifier.padding(horizontal = 1.dp)
                ) {
                    Text(
                        text = nota,
                        fontSize = 12.sp, // Tamaño del texto
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }

}

@Preview(showSystemUi = true)
@Composable
fun Preview() {
    MyApp()
}