package com.greenkart.domain.repository

import com.greenkart.domain.model.Order
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(order: Order): Resource<Unit>
    fun getOrders(userId: String): Flow<Resource<List<Order>>>
}
