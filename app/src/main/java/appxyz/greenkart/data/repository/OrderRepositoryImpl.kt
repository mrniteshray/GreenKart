package appxyz.greenkart.data.repository

import appxyz.greenkart.data.local.OrderDao
import appxyz.greenkart.domain.model.Order
import appxyz.greenkart.domain.repository.OrderRepository
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart

class OrderRepositoryImpl(
    private val orderDao: OrderDao
) : OrderRepository {

    override suspend fun placeOrder(order: Order): Resource<Unit> {
        return try {
            val orderWithId = order.copy(id = java.util.UUID.randomUUID().toString())
            orderDao.insertOrder(orderWithId)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to place order")
        }
    }

    override fun getOrders(userId: String): Flow<Resource<List<Order>>> {
        return orderDao.getOrders(userId)
            .map { orders -> Resource.Success(orders) as Resource<List<Order>> }
            .onStart { emit(Resource.Loading()) }
            .catch { e -> emit(Resource.Error(e.message ?: "Failed to fetch orders")) }
    }
}

