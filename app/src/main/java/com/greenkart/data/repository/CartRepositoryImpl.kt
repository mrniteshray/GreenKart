package com.greenkart.data.repository

import com.greenkart.data.local.CartDao
import com.greenkart.domain.model.CartItem
import com.greenkart.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> = cartDao.getCartItems()

    override suspend fun addToCart(item: CartItem) {
        val existingItem = cartDao.getCartItemById(item.id)
        if (existingItem != null) {
            cartDao.updateQuantity(item.id, existingItem.quantity + 1)
        } else {
            cartDao.insertCartItem(item)
        }
    }

    override suspend fun removeFromCart(item: CartItem) {
        cartDao.deleteCartItem(item)
    }

    override suspend fun updateQuantity(itemId: String, quantity: Int) {
        if (quantity <= 0) {
            // Should probably delete, but simple update for now
            // cartDao.updateQuantity(itemId, 0)
        } else {
            cartDao.updateQuantity(itemId, quantity)
        }
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
