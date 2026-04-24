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
                onNavigateToEdit = { id -> navHostController.navigate(Screen.ProductoEdit(id)) },
                onNavigateToLogin = { 
                    navHostController.navigate(Screen.Login) {
                        popUpTo(0) { inclusive = true } // CLEARS ENTIRE BACKSTACK
                        launchSingleTop = true
                    }
                },
                onNavigateToDirecciones = { navHostController.navigate(Screen.DireccionesList) },
                onNavigateToMetodosPago = { navHostController.navigate(Screen.MetodosPagoList) },
                onNavigateToCarrito = { navHostController.navigate(Screen.Carrito) }
            )
        }

        composable<Screen.Carrito> {
            edu.ucne.dulcedeleite.presentation.carrito.CarritoScreen(
                onNavigateBack = { navHostController.navigateUp() },
                onNavigateToExplorar = { navHostController.navigateUp() },
                onNavigateToCheckout = { navHostController.navigate(Screen.ResumenPedido) }
            )
        }

        composable<Screen.ResumenPedido> {
            edu.ucne.dulcedeleite.presentation.resumenpedido.ResumenPedidoScreen(
                onNavigateBack = { navHostController.navigateUp() },
                onCheckoutSuccess = { navHostController.navigate(Screen.Dashboard) { popUpTo(Screen.Dashboard) { inclusive = true } } }
            )
        }

        composable<Screen.ProductoDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.ProductoDetail>()
            DetailScreen(
                id = args.id,
                onBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.ProductoEdit> { backStackEntry ->
            edu.ucne.dulcedeleite.presentation.proyecto.edit.ProductoEditScreen(
                onNavigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.DireccionesList> {
            edu.ucne.dulcedeleite.presentation.direccion.list.DireccionesListScreen(
                onNavigateToForm = { id -> navHostController.navigate(Screen.DireccionForm(id)) }
            )
        }

        composable<Screen.DireccionForm> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.DireccionForm>()
            edu.ucne.dulcedeleite.presentation.direccion.form.DireccionFormScreen(
                id = args.id,
                onNavigateBack = { navHostController.navigateUp() }
            )
        }

        composable<Screen.MetodosPagoList> {
            edu.ucne.dulcedeleite.presentation.metodopago.list.MetodosPagoListScreen(
                onNavigateToForm = { id -> navHostController.navigate(Screen.MetodoPagoForm(id)) }
            )
        }

        composable<Screen.MetodoPagoForm> { backStackEntry ->
            val args = backStackEntry.toRoute<Screen.MetodoPagoForm>()
            edu.ucne.dulcedeleite.presentation.metodopago.form.MetodoPagoFormScreen(
                id = args.id,
                onNavigateBack = { navHostController.navigateUp() }
            )
        }
    }
}