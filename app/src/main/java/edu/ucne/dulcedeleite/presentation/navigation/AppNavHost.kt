package edu.ucne.dulcedeleite.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute


import edu.ucne.dulcedeleite.presentation.proyecto.list.ListScreen
import edu.ucne.dulcedeleite.presentation.proyecto.detail.DetailScreen
import edu.ucne.dulcedeleite.presentation.login.LoginScreen
import edu.ucne.dulcedeleite.presentation.signup.SignUpScreen

@Composable
fun AppNavHost(navHostController: NavHostController) {

    NavHost(navController = navHostController, startDestination = Screen.Login) {

        composable<Screen.Login> {
            LoginScreen(
                onNavigateToSignUp = { navHostController.navigate(Screen.SignUp) },
                onNavigateToHome = { navHostController.navigate(Screen.Dashboard) {
                    popUpTo(Screen.Login) { inclusive = true }
                } }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                onNavigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.Dashboard> {
            edu.ucne.dulcedeleite.presentation.home.MainScreen(
                onNavigateToDetail = { id -> navHostController.navigate(Screen.ProductoDetail(id)) },
                onNavigateToEdit = { id -> navHostController.navigate(Screen.ProductoEdit(id)) }
            )
        }

        composable<Screen.ProductoDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ProductoDetail>()
            DetailScreen(
                id = args.id,
                onBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.ProductoEdit> {
            edu.ucne.dulcedeleite.presentation.proyecto.edit.ProductoEditScreen(
                onNavigateBack = { navHostController.navigateUp() }
            )
        }
    }
}