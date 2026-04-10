package edu.ucne.dulcedeleite.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute


import edu.ucne.dulcedeleite.presentation.proyecto.list.ListScreen
import edu.ucne.dulcedeleite.presentation.proyecto.detail.DetailScreen

@Composable
fun AppNavHost(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Screen.ProductoList) {

        composable<Screen.ProductoList> {
            ListScreen(
                onNavigateToDetail = { id -> navHostController.navigate(Screen.ProductoDetail(id)) }
            )
        }

        composable<Screen.ProductoDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ProductoDetail>()
            DetailScreen(
                id = args.id,
                onBack = { navHostController.navigateUp() }
            )
        }
    }
}