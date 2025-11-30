package com.miempresa.mowimarket.data.models

data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val images: List<String> = emptyList(),
    val category: String,
    val brand: String? = null,
    val rating: Float = 0f,
    val reviewCount: Int = 0,
    val stock: Int = 0,
    val isFeatured: Boolean = false
) {
    val hasDiscount: Boolean
        get() = originalPrice != null && originalPrice > price

    val discountPercentage: Int
        get() = if (hasDiscount && originalPrice != null) {
            (((originalPrice - price) / originalPrice) * 100).toInt()
        } else 0

    val formattedPrice: String
        get() = "S/ ${String.format("%.2f", price)}"

    val formattedOriginalPrice: String?
        get() = originalPrice?.let { "S/ ${String.format("%.2f", it)}" }
}
