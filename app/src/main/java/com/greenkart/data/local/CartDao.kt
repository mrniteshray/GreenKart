package com.greenkart.data.local

import androidx.room.*
import com.greenkart.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cart_items")
    fun getCartItems(): Flow<List<CartItem>>

    @Query("SELECT * FROM cart_items WHERE id = :itemId")
    suspend fun getCartItemById(itemId: String): CartItem?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem)

    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    @Query("UPDATE cart_items SET quantity = :quantity WHERE id = :itemId")
    suspend fun updateQuantity(itemId: String, quantity: Int)

    @Query("DELETE FROM cart_items")
    suspend fun clearCart()
}
