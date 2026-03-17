package com.greenkart.domain.repository

import com.greenkart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addToCart(item: CartItem)
    suspend fun removeFromCart(item: CartItem)
    suspend fun updateQuantity(itemId: String, quantity: Int)
    suspend fun clearCart()
}
