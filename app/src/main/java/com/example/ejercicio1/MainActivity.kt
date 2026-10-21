package com.example.ejercicio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ejercicio1.ui.screens.ProductDetailScreen
import com.example.ejercicio1.ui.screens.ProductListScreen
import com.example.ejercicio1.ui.theme.Ejercicio1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Ejercicio1Theme {
                ShopApp()
            }
        }
    }
}

@Composable
fun ShopApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "productos") {
        composable("productos") {
            ProductListScreen(
                onProductClick = { id -> navController.navigate("producto/$id") }
            )
        }
        composable(
            "producto/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            ProductDetailScreen(
                productId = it.arguments!!.getInt("id"),
                onBack = { navController.popBackStack() }
            )
        }
    }
}