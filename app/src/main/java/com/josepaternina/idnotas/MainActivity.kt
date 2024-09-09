package com.josepaternina.idnotas

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.josepaternina.idnotas.GlobalStateTonal.numActual
import com.josepaternina.idnotas.GlobalStateTonal.tonalidadActual
import kotlinx.coroutines.delay

// 1era de las tonalidades ("C", "D", "E", "F", "G", "A", "B")
val tonalidades = listOf("C", "D", "E", "F", "G", "A", "B")
val num = listOf(1, 2, 3, 4, 5, 6)

// Tonalidades de C, D y G
val allNotas = listOf(
    listOf("C", "Dm", "Em", "F", "G", "Am"),
    listOf("D", "Em", "F#m", "G", "A", "Bm"),
    listOf("E", "F#m", "G#m", "A", "B", "C#m"),
    listOf("F", "Gm", "Am", "Bb", "C", "Dm"),
    listOf("G", "Am", "Bm", "C", "D", "Em"),
    listOf("A", "Bm", "C#m", "D", "E", "F#m"),
    listOf("B", "C#m", "D#m", "E", "F#", "G#m")
)

// Puntuación
var puntos by mutableIntStateOf(0)
var errores by mutableIntStateOf(0)
var showImage: String by mutableStateOf("") // Imagen temporal: OK o error

// Nota seleccionada
var notaSelect: String by mutableStateOf("")
var notaCorrecta: String by mutableStateOf("")

//CheckBox                                                      C     D     E      F      G     A      B
val isCheckedNota = mutableStateListOf(true, false, false, false, false, false, false)
var practiceTime by mutableIntStateOf(0) // Tiempo de conteo regresivo
var showDialogResult by mutableStateOf(false) // Muestra los resultados (aciertos y fallos)

class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fijar la orientación en vertical
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            MyContent()
        }
    }
}

@Composable
fun MyContent() {
    var showDialogInsertTime by remember { mutableStateOf(false) } // Para

    // Texto descriptivo
    val textDescrip = if (notaSelect != "") {
        "Nota seleccionada: $notaSelect"
    } else {
        "Seleccione la nota correspondiente!"
    }

    // Imagen temporal: OK o error
    TimedImage()

    // Diálogo de resultados
    if (showDialogResult) {
        DialogResult(
            title = "Resultados de la práctica",
            message = "Cantidad de aciertos: $puntos \n" +
                    "Cantidad de errores: $errores \n" +
                    "Tiempo: $practiceTime segundos",
            onConfirm = {
                showDialogResult = false // Ocultar visualización de resultados
                practiceTime = 0 // Temporizador en cero
            }
        )
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado: Aciertos y errores
        Header()

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f), // Ocupa el espacio disponible
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            // Temporizador
            if (practiceTime > 0) {
                CountdownTimer(practiceTime) // Temporizador
            } else {
                // Botón de nuevo reto
                Button(onClick = {
                    showDialogInsertTime = true
                }) {
                    Text(text = "JUGAR", fontSize = 20.sp)
                }
            }

            // Diálogo para insertar el tiempo
            if (showDialogInsertTime) {
                DialogInsertTime(
                    title = "Tiempo de práctica",
                    message = "Por favor, ingrese el tiempo:",
                    onDismiss = { showDialogInsertTime = false },
                    onConfirm = { input ->
                        showDialogInsertTime = false
                        practiceTime = input.toInt()
                        puntos = 0
                        errores = 0
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.1f), // Ocupa el espacio disponible,
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Notas seleccionadas
                CheckBoxAction()
                Column(
                    //verticalArrangement = Arrangement.Center,
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
        }
        Footer()
    }

}

// Singleton object que almacena la tonalidad actual
object GlobalStateTonal {
    // variable global reactiva
    var tonalidadActual by mutableStateOf(tonalidades.filterIndexed { index, _ -> isCheckedNota[index] }
        .random())
    var numActual by mutableIntStateOf(num.random())

    // Función para cambiar la tonalidad aleatoriamente
    fun cambiarTonalidad() {
        tonalidadActual = tonalidades.filterIndexed { index, _ -> isCheckedNota[index] }.random()
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
        arrayListOf("C#m", "D#m", "F#m", "G#m", "A#m", "F#", "Bb")
    )
    val context = LocalContext.current
    var mediaPlayer: MediaPlayer // Sonido: correcto o incorrecto
    var bgColor: Color // Color de fondo de los botones
    var textColor: Color  // Color del texto de los botones
    var sizeText: TextUnit // Tamaño del texto de los botones


    // Puntuación
    fun puntuacion() {
        // Si hay alguna tonalidad para practicar seleccionada
        if (isCheckedNota.any { it }) {
            allNotas.forEach { nota ->
                if (nota[0] == tonalidadActual) {
                    notaCorrecta = nota[numActual - 1]

                    if (notaSelect == nota[numActual - 1]) {
                        // Sonido: correcto
                        mediaPlayer = MediaPlayer.create(context, R.raw.correct_choice)
                        mediaPlayer.start() // Reproducir sonido
                        puntos += 1
                        showImage = "ok"
                    } else {
                        // Sonido: incorrecto
                        mediaPlayer = MediaPlayer.create(context, R.raw.wrong_answer)
                        mediaPlayer.start() // Reproducir sonido
                        errores += 1
                        showImage = "error"
                    }
                    // Liberar recursos después de que termine el sonido
                    mediaPlayer.setOnCompletionListener { mp ->
                        mp.release()
                    }
                }
            }

            // Llamar a cambiar tonalidad
            GlobalStateTonal.cambiarTonalidad()
        } else {
            Toast.makeText(context, "Seleccione una tonalidad", Toast.LENGTH_SHORT).show()
        }

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
                        bgColor = MaterialTheme.colorScheme.primary
                        sizeText = 18.sp
                    } else if (notas[0] == "Cm") {
                        bgColor = DarkGray
                        sizeText = 16.sp
                    } else {
                        bgColor = Gray
                        sizeText = 13.sp
                    }
                    // Color temporal del botón de la nota correcta
                    if (notaCorrecta == nota) {
                        bgColor = Green
                        textColor = Black
                    } else {
                        textColor = White
                    }

                    // Botones de las notas determinadas
                    TextButton(
                        onClick = {
                            if (practiceTime > 0) {
                                notaSelect = nota
                                puntuacion()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Presione 'JUGAR' para ingresar el tiempo",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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
                                color = textColor, fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TimedImage() {
    val myImagen: Painter
    // Si no se ha iniciado no se muestra ninguna imagen: OK o error
    if (notaSelect != "") {
        myImagen = if (showImage == "ok") {
            painterResource(id = R.drawable.ok) // img Ok
        } else {
            painterResource(id = R.drawable.error) // img error
        }

        if (showImage != "") {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = myImagen, contentDescription = "Respuesta",
                    Modifier
                        .size(180.dp)
                        .padding(top = 100.dp),
                    Alignment.Center
                )
            }

            //Delay
            LaunchedEffect(Unit) {
                delay(500) // Espera 500 ms
                showImage = "" // Para que se quite la imagen
                notaCorrecta = "" // Para que se quite el color temporal del botón
            }
        }
    }
}

@Composable
fun CheckBoxAction() {
    // Tonalidades para practicar
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tonalidades para practicar:",
            fontSize = 20.sp
        )

        Row {
            for (index in isCheckedNota.indices) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(0.dp)
                ) {
                    Checkbox(
                        checked = isCheckedNota[index],
                        onCheckedChange = { it ->
                            isCheckedNota[index] = it
                            // Si al menos está seleccionada una tonalidad, se puede cambiar
                            if (isCheckedNota.any { it }) GlobalStateTonal.cambiarTonalidad()
                            notaSelect = "" // Resetea la nota oprimida
                        }
                    )

                    Text(
                        text = tonalidades[index], fontSize = 20.sp,
                        modifier = Modifier.padding(0.dp)
                    )
                }
            }
        }
    }
}

// Header
@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween

    ) {
        Text(
            text = "Aciertos: $puntos", fontSize = 18.sp, style = TextStyle(
                color = White, fontWeight = FontWeight.Bold
            )
        )
        Text(
            text = "Errores: $errores", fontSize = 18.sp, style = TextStyle(
                color = White, fontWeight = FontWeight.Bold
            )
        )
    }
}

// Dialogo de ingreso del tiempo temporizador
@Composable
fun DialogInsertTime(
    title: String,
    message: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var entrada by remember { mutableStateOf("") }
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title)
        },
        text = {
            Column {
                Text(text = message, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = entrada,
                    onValueChange = { entrada = it },
                    label = { Text("Ingrese el tiempo en segundos") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number) // Teclado numérico

                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    // Si la entrada se puede convertir a número y está dentro del rango
                    if (entrada.toIntOrNull() != null && entrada.toInt() in 10..300) {
                        onConfirm(entrada)
                    } else {
                        Toast.makeText(
                            context, "Ingrese un número válido \n" +
                                    "(De 10 a 300 segundos)", Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onDismiss() }
            ) {
                Text("Cancelar")
            }
        }
    )
}

// Temporizador
@Composable
fun CountdownTimer(totalTimeInSeconds: Int) {
    var timeLeft by remember { mutableIntStateOf(totalTimeInSeconds) }
    var progress by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L) // Espera 1 segundo
            timeLeft--
            progress = timeLeft / totalTimeInSeconds.toFloat()
        }
    }

    // Cuando se termine el tiempo del temporizador
    if (timeLeft == 0) {
        showDialogResult = true // Muestra el diálogo de resultados
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Tiempo restante: $timeLeft ${if (timeLeft > 1) "segundos" else "segundo"}",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        )
    }
}

// Muestra de resultados
@Composable
fun DialogResult(
    title: String,
    message: String,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = title)
        },
        text = {
            Column {
                Text(text = message, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm() }
            ) {
                Text("OK")
            }
        }
    )
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