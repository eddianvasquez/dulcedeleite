package edu.ucne.dulcedeleite.presentation.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.ucne.dulcedeleite.presentation.navigation.Screen
import edu.ucne.dulcedeleite.presentation.nosotros.NosotrosScreen
import edu.ucne.dulcedeleite.presentation.pedidos.PedidosScreen
import edu.ucne.dulcedeleite.presentation.perfil.PerfilScreen
import edu.ucne.dulcedeleite.presentation.proyecto.list.ListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToDetail: (Int) -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    val bottomNavController = rememberNavController()
    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Determinamos si estamos en la ruta HomeMain
    val isHome = currentDestination?.route?.contains("HomeMain") == true

    Scaffold(
        topBar = {
            if (isHome) {
                CenterAlignedTopAppBar(
                    title = {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            placeholder = { Text("Buscar dulces...") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .padding(horizontal = 8.dp),
                            singleLine = true,
                            shape = MaterialTheme.shapes.extraLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = { /* TODO: Carrito */ }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Carrito")
                        }
                    }
                )
            }
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Pedidos") },
                    label = { Text("Pedidos") },
                    selected = currentDestination?.route?.contains("Pedidos") == true,
                    onClick = {
                        bottomNavController.navigate(Screen.Pedidos) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = currentDestination?.route?.contains("HomeMain") == true,
                    onClick = {
                        bottomNavController.navigate(Screen.HomeMain) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Info, contentDescription = "Nosotros") },
                    label = { Text("Nosotros") },
                    selected = currentDestination?.route?.contains("Nosotros") == true,
                    onClick = {
                        bottomNavController.navigate(Screen.Nosotros) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = currentDestination?.route?.contains("Perfil") == true,
                    onClick = {
                        bottomNavController.navigate(Screen.Perfil) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = Screen.HomeMain,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<Screen.Pedidos> {
                PedidosScreen()
            }
            composable<Screen.HomeMain> {
                ListScreen(
                    onNavigateToDetail = onNavigateToDetail,
                    onNavigateToEdit = onNavigateToEdit
                )
            }
            composable<Screen.Nosotros> {
                NosotrosScreen()
            }
            composable<Screen.Perfil> {
                PerfilScreen()
            }
        }
    }
}
