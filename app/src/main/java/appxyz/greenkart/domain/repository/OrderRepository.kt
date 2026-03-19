package appxyz.greenkart.domain.repository

import appxyz.greenkart.domain.model.Order
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun placeOrder(order: Order): Resource<Unit>
    fun getOrders(userId: String): Flow<Resource<List<Order>>>
}

