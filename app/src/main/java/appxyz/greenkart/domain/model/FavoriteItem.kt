package appxyz.greenkart.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteItem(
    @PrimaryKey
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val originalPrice: Double = 0.0,
    val unit: String = "",
    val imageUrl: String = "",
    val categoryId: String = "",
    val isOrganic: Boolean = false,
    val rating: Double = 0.0,
    val deliveryTime: String = "",
    val description: String = ""
)

