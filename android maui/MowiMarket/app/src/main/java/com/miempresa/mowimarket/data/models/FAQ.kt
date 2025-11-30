package com.miempresa.mowimarket.data.models

data class FAQ(
    val id: String,
    val question: String,
    val answer: String,
    val category: FAQCategory
)

enum class FAQCategory(val displayName: String) {
    ALL("Todos"),
    ORDERS("Pedidos"),
    PAYMENTS("Pagos"),
    RETURNS("Devoluciones"),
    ACCOUNT("Cuenta")
}
