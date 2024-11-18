package com.josepaternina.idnotas

sealed class ItemsMenu(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    data object Pantalla1 : ItemsMenu(R.drawable.practice, "Práctica", "pantalla1")
    data object Pantalla2 : ItemsMenu(R.drawable.music_note, "Nota", "pantalla2")
    data object Pantalla3 : ItemsMenu(R.drawable.library_music, "Todas las notas", "pantalla3")
}