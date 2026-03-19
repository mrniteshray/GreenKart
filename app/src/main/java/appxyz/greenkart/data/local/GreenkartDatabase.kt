package appxyz.greenkart.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import appxyz.greenkart.domain.model.CartItem
import appxyz.greenkart.domain.model.FavoriteItem
import appxyz.greenkart.domain.model.Order
import appxyz.greenkart.domain.model.OrderConverters

@Database(entities = [CartItem::class, Order::class, FavoriteItem::class], version = 3)
@TypeConverters(OrderConverters::class)
abstract class GreenkartDatabase : RoomDatabase() {
    abstract val cartDao: CartDao
    abstract val orderDao: OrderDao
    abstract val favoriteDao: FavoriteDao
}

