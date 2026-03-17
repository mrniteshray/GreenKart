package com.greenkart.data.local

import androidx.room.*
import com.greenkart.domain.model.Order
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY timestamp DESC")
    fun getOrders(userId: String): Flow<List<Order>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("DELETE FROM orders")
    suspend fun clearOrders()
}
