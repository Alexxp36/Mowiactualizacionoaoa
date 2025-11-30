package com.miempresa.mowimarket.data.models

data class CartItem(
    val id: String,
    val product: Product,
    val quantity: Int = 1
) {
    val subtotal: Double
        get() = product.price * quantity

    val formattedSubtotal: String
        get() = "S/ ${String.format("%.2f", subtotal)}"
}

data class Cart(
    val items: List<CartItem> = emptyList()
) {
    val subtotal: Double
        get() = items.sumOf { it.subtotal }

    val tax: Double
        get() = subtotal * 0.18 // 18% IGV en Perú

    val total: Double
        get() = subtotal + tax

    val itemCount: Int
        get() = items.sumOf { it.quantity }

    val isEmpty: Boolean
        get() = items.isEmpty()

    val formattedSubtotal: String
        get() = "S/ ${String.format("%.2f", subtotal)}"

    val formattedTax: String
        get() = "S/ ${String.format("%.2f", tax)}"

    val formattedTotal: String
        get() = "S/ ${String.format("%.2f", total)}"
}
