package edu.ucne.dulcedeleite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.dulcedeleite.presentation.navigation.AppNavHost
import edu.ucne.dulcedeleite.ui.theme.DulceDeleiteTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DulceDeleiteTheme {

                val navController = rememberNavController()

                AppNavHost(navHostController = navController)
            }
        }
    }
}