package com.josepaternina.idnotas

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SelectTonalidad() {
    var selectedNota by remember { mutableStateOf("C") }
    //var selectedNotaIndex by remember { mutableStateOf(0) }
    var selectedNotaIndex by remember { mutableIntStateOf(0) }

    val notasMusicales = listOf("C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B")
    val tonalidades = listOf(
        listOf("C", "Dm", "Em", "F", "G", "Am", "B°"),
        listOf("C#", "D#m", "Fm", "F#", "G#", "A#m", "C°"),
        listOf("D", "Em", "F#m", "G", "A", "Bm", "C#°"),
        listOf("D#", "Fm", "Gm", "G#", "A#", "Cm", "D°"),
        listOf("E", "F#m", "G#m", "A", "B", "C#m", "D#°"),
        listOf("F", "Gm", "Am", "A#", "C", "Dm", "E°"),
        listOf("F#", "G#m", "A#m", "B", "C#", "D#m", "F°"),
        listOf("G", "Am", "Bm", "C", "D", "Em", "F#°"),
        listOf("G#", "A#m", "Cm", "C#", "D#", "Fm", "G°"),
        listOf("A", "Bm", "C#m", "D", "E", "F#m", "G#°"),
        listOf("A#", "Cm", "Dm", "D#", "F", "Gm", "A°"),
        listOf("B", "C#m", "D#m", "E", "F#", "G#m", "A#°")
    )

    Column(modifier = Modifier.padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        // Selector de Nota
        Text(text = "Selecciona una nota:", style = TextStyle(fontWeight = FontWeight.Bold))
        DropdownMenu(
            notasMusicales = notasMusicales,
            selectedNota = selectedNota,
            onNotaSelected = { nota ->
                selectedNota = nota
                selectedNotaIndex = notasMusicales.indexOf(nota)
            }
        )

        // Tabla encabezado del 1 añl 7
        Spacer(modifier = Modifier.height(16.dp))
        LettersTableHeader()

        // Tabla de Notas
        NotesTable(tonalidades[selectedNotaIndex])

    }
}

@Composable
fun DropdownMenu(
    notasMusicales: List<String>,
    selectedNota: String,
    onNotaSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.widthIn(min = 200.dp)) {  // Establecemos un mínimo para que no sea demasiado pequeño
        TextField(
            value = selectedNota,
            onValueChange = {},
            label = { Text("Seleccione una nota") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF1F1F1), shape = RoundedCornerShape(8.dp)),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = "Toggle dropdown",
                        tint = Color(0xFF6200EE)
                    )
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color(0xFF6200EE),
                unfocusedIndicatorColor = Color(0xFFBBBBBB)
            ),
            shape = RoundedCornerShape(8.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .wrapContentWidth()  // Ajuste al tamaño del contenido del menú
                .background(Color.White)
                .padding(4.dp)
        ) {
            notasMusicales.forEach { nota ->
                DropdownMenuItem(
                    onClick = {
                        onNotaSelected(nota)
                        expanded = false
                    },
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        text = nota,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }
    }
}


@Composable
fun NotesTable(tonalidad: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary) // Fondo color primario
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            tonalidad.forEach { nota ->
                Text(
                    text = nota,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.White // Texto blanco para contraste
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .border(
                            BorderStroke(1.dp, Color.White),
                            RoundedCornerShape(4.dp)
                        ) // Borde blanco
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun LettersTableHeader() {
    Column(modifier = Modifier.fillMaxWidth()) {
        val numNote = listOf("1", "2", "3", "4", "5", "6", "7")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary) // Fondo color primario
                .padding(horizontal = 8.dp, vertical = 2.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            numNote.forEach { letra ->
                Text(
                    text = letra,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.White // Texto blanco para contraste
                    ),
                    modifier = Modifier
                        .padding(4.dp)
                        .border(
                            BorderStroke(1.dp, Color.White),
                            RoundedCornerShape(4.dp)
                        ) // Borde blanco
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun DefaultPreview() {
    SelectTonalidad()
}
