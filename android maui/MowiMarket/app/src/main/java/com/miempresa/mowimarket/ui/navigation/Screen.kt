package com.miempresa.mowimarket.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Products : Screen("products")
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }
    object Cart : Screen("cart")
    object Support : Screen("support")
    object Auth : Screen("auth")
    object Profile : Screen("profile")
}
