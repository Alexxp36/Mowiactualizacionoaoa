package com.miempresa.mowimarket.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Products : Screen("products?categoryId={categoryId}&categoryName={categoryName}") {
        fun createRoute(categoryId: String? = null, categoryName: String? = null): String {
            return if (categoryId != null && categoryName != null) {
                "products?categoryId=$categoryId&categoryName=$categoryName"
            } else {
                "products"
            }
        }
    }
    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }
    object Cart : Screen("cart")
    object Checkout : Screen("checkout")
    object Support : Screen("support")
    object Auth : Screen("auth")
    object Profile : Screen("profile")

    // Admin routes
    object AdminDashboard : Screen("admin/dashboard")
    object AdminProducts : Screen("admin/products")
    object AdminAddProduct : Screen("admin/products/add")
    object AdminPedidos : Screen("admin/pedidos")
}
