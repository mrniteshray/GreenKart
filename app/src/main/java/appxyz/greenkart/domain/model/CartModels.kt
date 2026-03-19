package appxyz.greenkart.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val unit: String = "",
    val imageUrl: String = "",
    val quantity: Int = 1
)

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val id: String = "",
    val userId: String = "",
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val timestamp: Long = System.currentTimeMillis(),
    val status: String = "Placed",
    val deliveryAddress: String = ""
)

class OrderConverters {
    private val gson = com.google.gson.Gson()

    @androidx.room.TypeConverter
    fun fromCartItemList(value: List<CartItem>?): String {
        return gson.toJson(value)
    }

    @androidx.room.TypeConverter
    fun toCartItemList(value: String): List<CartItem> {
        val listType = object : com.google.gson.reflect.TypeToken<List<CartItem>>() {}.type
        return gson.fromJson(value, listType)
    }
}

