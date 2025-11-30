package com.miempresa.mowimarket.data.models

data class Category(
    val id: String,
    val name: String,
    val iconName: String,
    val productCount: Int = 0
)

// Categorías predefinidas
object Categories {
    val TECHNOLOGY = Category("tech", "Tecnología", "smartphone", 0)
    val FASHION = Category("fashion", "Moda", "checkroom", 0)
    val HOME = Category("home", "Hogar", "home", 0)
    val PETS = Category("pets", "Mascotas", "pets", 0)
    val BABIES = Category("babies", "Bebés", "child_care", 0)
    val TOYS = Category("toys", "Juguetes", "sports_esports", 0)

    val ALL = listOf(TECHNOLOGY, FASHION, HOME, PETS, BABIES, TOYS)
}
