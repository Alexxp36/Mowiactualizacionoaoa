package com.miempresa.mowimarket.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.miempresa.mowimarket.ui.screens.auth.AuthScreen
import com.miempresa.mowimarket.ui.screens.cart.CartScreen
import com.miempresa.mowimarket.ui.screens.home.HomeScreen
import com.miempresa.mowimarket.ui.screens.products.ProductsScreen
import com.miempresa.mowimarket.ui.screens.support.SupportScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProducts = { category ->
                    navController.navigate(Screen.Products.route)
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        composable(Screen.Products.route) {
            ProductsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                }
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            // ProductDetailScreen will be implemented later
        }

        composable(Screen.Cart.route) {
            CartScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinueShopping = {
                    navController.navigate(Screen.Products.route) {
                        popUpTo(Screen.Home.route)
                    }
                }
            )
        }

        composable(Screen.Support.route) {
            SupportScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.popBackStack()
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }
    }
}
