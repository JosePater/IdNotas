package com.josepaternina.idnotas

//import androidx.compose.material3.rememberBottomSheetScaffoldState
//import androidx.compose.runtime.remember
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.josepaternina.idnotas.ItemsMenu.Pantalla1
import com.josepaternina.idnotas.ItemsMenu.Pantalla2
import com.josepaternina.idnotas.ItemsMenu.Pantalla3


class MainActivity : ComponentActivity() {
    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Fijar la orientación en vertical
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            PantallaPrincipal()
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal() {
    val navController = rememberNavController()
    //val scaffoldState = rememberBottomSheetScaffoldState()
    //val scope = rememberCoroutineScope()

    val navigationItem = listOf(Pantalla1, Pantalla2, Pantalla3)

    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(text = "IDENTIFICADOR DE NOTAS")
                }
            )
        },
        bottomBar = { NavegacionInferior(navController, navigationItem) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Contenido
            NavigationHost(navController)
        }

    }
}

// De vuelve la ruta de nav que está seleccionada
@Composable
fun currentRoute(navController: NavHostController): String? {
    val entrada by navController.currentBackStackEntryAsState()
    return entrada?.destination?.route
}


// Navegación Inferior
@Composable
fun NavegacionInferior(
    navController: NavHostController,
    menuItems: List<ItemsMenu>
) {
    // BottomAppBar {
    BottomNavigation {
        val currentRoute = currentRoute(navController = navController)
        menuItems.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.ruta,
                onClick = { navController.navigate(item.ruta) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(item.title, color = Color.White) },
                alwaysShowLabel = false
            )
        }
    }
    //}
}

// Preview
@Preview(showSystemUi = true)
@Composable
fun PreviewPrincipal() {
    PantallaPrincipal()
}

