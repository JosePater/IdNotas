package com.josepaternina.idnotas

import android.media.MediaPlayer
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
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.josepaternina.idnotas.GlobalStateTonal.numActual
import com.josepaternina.idnotas.GlobalStateTonal.tonalidadActual

// 1era de las tonalidades ("C", "D", "E", "F", "G", "A", "B")
val tonalidades = listOf("C", "D", "G")
val num = listOf(1, 2, 3, 4, 5, 6)

// Tonalidades de C, D y G
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
            MyContent()
        }
    }
}

@Composable
fun MyContent() {
    // Texto descriptivo
    val textDescrip = if (notaSelect != "") {
        "Nota seleccionada: $notaSelect"
    } else {
        "Seleccione la nota correspondiente!"
    }

    // UI
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp), contentAlignment = Alignment.Center

    ) {
        Column(
            Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text(text = "Aciertos: $puntos", fontSize = 18.sp)
                Text(text = "Errores: $errores", fontSize = 18.sp)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Tonalidad: $tonalidadActual", fontSize = 22.sp)
                    Text(text = "# $numActual", fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = textDescrip,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 16.dp),
                    )
                    BotonesDeNotas()
                }
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


// Botones de las notas mayores, menores y sostenidas
@Composable
fun BotonesDeNotas() {
    // Notas de todos los botones
    val notesForButtons = arrayListOf(
        arrayListOf("C", "D", "E", "F", "G", "A", "B"),
        arrayListOf("Cm", "Dm", "Em", "Fm", "Gm", "Am", "Bm"),
        arrayListOf("C#m", "D#m", "F#m", "G#m", "A#m")
    )
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer // Sonido: correcto o incorrecto
    var bgColor: Color // Color de fondo de los botones
    var sizeText: TextUnit // Tamaño del texto de los botones

    // Puntuación
    fun puntuacion() {
        allNotas.forEach { nota ->
            if (nota[0] == tonalidadActual) {
                if (notaSelect == nota[numActual - 1]) {
                    // Sonido: correcto
                    mediaPlayer = MediaPlayer.create(context, R.raw.correct_choice)
                    mediaPlayer.start() // Reproducir sonido
                    puntos += 1
                    Toast.makeText(context, "✅ OK", Toast.LENGTH_SHORT).show()
                } else {
                    // Sonido: incorrecto
                    mediaPlayer = MediaPlayer.create(context, R.raw.wrong_answer)
                    mediaPlayer.start() // Reproducir sonido
                    errores += 1
                    Toast.makeText(
                        context,
                        "❌ \n$tonalidadActual$numActual: ${nota[numActual - 1]}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                // Liberar recursos después de que termine el sonido
                mediaPlayer.setOnCompletionListener { mp ->
                    mp.release()
                }
            }
        }
        // Llamar a cambiar tonalidad
        GlobalStateTonal.cambiarTonalidad()
    }

    //Notas
    Column(
        verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        notesForButtons.forEach { notas ->
            Row {
                notas.forEach { nota ->
                    // bgColor y fontSize según el tipo de notas (C, Cm, C#m)
                    if (notas[0] == "C") {
                        bgColor = Blue
                        sizeText = 18.sp
                    } else if (notas[0] == "Cm") {
                        bgColor = DarkGray
                        sizeText = 16.sp
                    } else {
                        bgColor = Gray
                        sizeText = 13.sp
                    }

                    // Botones de las notas determinadas
                    TextButton(
                        onClick = {
                            notaSelect = nota
                            puntuacion()
                        }, modifier = Modifier
                            .width(56.dp) // Establece el ancho del botón
                            .height(56.dp) // Establece la altura del botón
                            .padding(2.dp) // Aplica el margen interno del botón
                            .clip(CircleShape) // Aplica círculo
                            .background(bgColor) // Color del círculo
                    ) {
                        Text(
                            text = nota,
                            fontSize = sizeText,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                color = Color.White, fontWeight = FontWeight.Bold
                            )
                        )
                    }
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
            fontSize = 13.sp,
            modifier = Modifier.align(Alignment.BottomCenter) // Alinea el texto en la parte inferior central
        )
    }
}

// Preview
@Preview(showSystemUi = true)
@Composable
fun Preview() {
    MyContent()
}