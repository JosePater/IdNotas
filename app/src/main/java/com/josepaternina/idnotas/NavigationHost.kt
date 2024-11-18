package com.josepaternina.idnotas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.josepaternina.idnotas.ItemsMenu.*

@Composable
fun NavigationHost(navController: NavHostController) {

    // Home
    NavHost(
        navController = navController,
        startDestination = Pantalla1.ruta
    ) {
        // Pantalla 1
        composable(Pantalla1.ruta) {
            PlayNotes()
        }

        // Pantalla2
        composable(Pantalla2.ruta) {
            Tonalidad()
        }

        // Pantalla2
        composable(Pantalla3.ruta) {
            AllTonalidades()
        }

    }
}

