package com.josepaternina.idnotas

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.josepaternina.idnotas.GlobalStateTonal.numActual
import com.josepaternina.idnotas.GlobalStateTonal.tonalidadActual

// Tonalidades ("C", "D", "E", "F", "G", "A", "B")
val tonalidades = listOf("C", "D", "G")
val num = listOf(1, 2, 3, 4, 5, 6)

val allNotas = listOf(
    listOf("C", "Dm", "Em", "F", "G", "Am"),
    listOf("D", "Em", "F#m", "G", "A", "Bm"),
    listOf("G", "Am", "Bm", "C", "D", "Em")
)

// Puntuación
var puntos by mutableIntStateOf(0)
var errores by mutableIntStateOf(0)

// Nota seleccionada
var notaSelect: String by mutableStateOf("")

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
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween

        ) {
            Text(text = "Aciertos: $puntos", fontSize = 16.sp)
            Text(text = "Errores: $errores", fontSize = 16.sp)
        }

        Text(
            text = "Nota seleccionada: $notaSelect",
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(1.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Tonalidad: $tonalidadActual", fontSize = 22.sp)
                Text(text = "# $numActual", fontSize = 20.sp)

                Spacer(modifier = Modifier.height(64.dp))
                NoteButtonsRow()

            }
            Footer()
        }
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
    val context = LocalContext.current

    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Puntuación
        fun puntuacion() {
            allNotas.forEach { nota ->
                if (nota[0] == tonalidadActual) {
                    if (notaSelect == nota[numActual - 1]) {
                        puntos += 1
                        Toast.makeText(context, "✅ OK", Toast.LENGTH_SHORT).show()
                    } else {
                        errores += 1

                        Toast.makeText(
                            context,
                            "❌ \n$tonalidadActual$numActual: ${nota[numActual - 1]}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            // Llamar a cambiar tonalidad
            GlobalStateTonal.cambiarTonalidad()
        }

        //Notas Mayores
        Row {
            notasMayores.forEach { nota ->
                TextButton(
                    onClick = {
                        notaSelect = nota
                        puntuacion()
                    }, modifier = Modifier
                        .width(56.dp) // Establece el ancho del botón
                        .height(56.dp) // Establece la altura del botón
                        .padding(2.dp) // Aplica el margen interno del botón
                        .clip(CircleShape)
                        .background(Color.Blue)
                ) {
                    Text(
                        text = nota,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        //Notas menores
        Row {
            notasMenores.forEach { nota ->
                TextButton(
                    onClick = {
                        notaSelect = nota
                        puntuacion()
                    }, modifier = Modifier
                        .width(56.dp) // Establece el ancho del botón
                        .height(56.dp) // Establece la altura del botón
                        .padding(2.dp) // Aplica el margen interno del botón
                        .clip(CircleShape) // Círculo
                        .background(Color.Magenta)
                ) {
                    Text(
                        text = nota,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // Notas menores #
        Row(
            horizontalArrangement = Arrangement.Center // Espacia uniformemente los botones
        ) {
            notasMenoresSos.forEach { nota ->
                TextButton(
                    onClick = {
                        notaSelect = nota
                        puntuacion()
                    },
                    //modifier = Modifier.padding(horizontal = 1.dp)
                    modifier = Modifier
                        .width(56.dp) // Establece el ancho del botón
                        .height(56.dp) // Establece la altura del botón
                        .padding(2.dp) // Aplica el margen interno del botón
                        .clip(CircleShape) // Círculo
                        .background(Color.Gray)

                ) {
                    Text(
                        text = nota,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    }

}

// Footer
@Composable
fun Footer() {
    Box(
        modifier = Modifier.padding(2.dp)
    ) {
        Text(
            text = "Desarrollador: José Paternina",
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.BottomCenter) // Alinea el texto en la parte inferior central
        )
    }
}

// Preview
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    MyApp()
}