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
                onNavigateToProducts = { categoryId ->
                    if (categoryId != null) {
                        // Find category name from Categories list
                        val category = com.miempresa.mowimarket.data.models.Categories.ALL.find { it.id == categoryId }
                        navController.navigate(Screen.Products.createRoute(categoryId, category?.name))
                    } else {
                        navController.navigate(Screen.Products.createRoute())
                    }
                },
                onNavigateToCart = {
                    navController.navigate(Screen.Cart.route)
                },
                onNavigateToAuth = {
                    navController.navigate(Screen.Auth.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                }
            )
        }

        composable(
            route = Screen.Products.route,
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                },
                navArgument("categoryName") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val categoryId = backStackEntry.arguments?.getString("categoryId")
            val categoryName = backStackEntry.arguments?.getString("categoryName")

            ProductsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                categoryId = categoryId,
                categoryName = categoryName
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
            val context = androidx.compose.ui.platform.LocalContext.current
            val mainViewModel = androidx.compose.runtime.remember { com.miempresa.mowimarket.ui.viewmodels.MainViewModel(context) }
            val currentUser by mainViewModel.currentUser.collectAsState()

            AuthScreen(
                onLoginSuccess = {
                    // Redirigir según el rol del usuario
                    val isAdmin = currentUser?.isAdmin == true || currentUser?.isStaff == true
                    if (isAdmin) {
                        navController.navigate(Screen.AdminDashboard.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    } else {
                        navController.popBackStack()
                    }
                },
                onDismiss = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.Profile.route) {
            com.miempresa.mowimarket.ui.screens.profile.ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onLogout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        // Admin Routes
        composable(Screen.AdminDashboard.route) {
            com.miempresa.mowimarket.ui.screens.admin.AdminDashboardScreen(
                onNavigateToProducts = {
                    navController.navigate(Screen.AdminProducts.route)
                },
                onNavigateToPedidos = {
                    navController.navigate(Screen.AdminPedidos.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.Profile.route)
                },
                onLogout = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.AdminProducts.route) {
            com.miempresa.mowimarket.ui.screens.admin.AdminProductsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Screen.AdminPedidos.route) {
            com.miempresa.mowimarket.ui.screens.admin.AdminPedidosScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
