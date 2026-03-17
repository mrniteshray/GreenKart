package com.greenkart.domain.model

data class Category(
    val id: String,
    val name: String,
    val imageUrl: String
)

data class Vegetable(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double,
    val unit: String,
    val categoryId: String,
    val imageUrl: String,
    val rating: Double,
    val deliveryTime: String,
    val isOrganic: Boolean
)
