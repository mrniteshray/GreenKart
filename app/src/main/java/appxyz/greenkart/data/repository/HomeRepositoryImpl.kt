package appxyz.greenkart.data.repository

import android.content.Context
import com.google.gson.Gson
import appxyz.greenkart.data.local.FavoriteDao
import appxyz.greenkart.domain.model.Category
import appxyz.greenkart.domain.model.FavoriteItem
import appxyz.greenkart.domain.model.Vegetable
import appxyz.greenkart.domain.repository.HomeRepository
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.InputStreamReader

class HomeRepositoryImpl(
    private val context: Context,
    private val gson: Gson,
    private val favoriteDao: FavoriteDao
) : HomeRepository {

    override fun getCategories(): Flow<Resource<List<Category>>> = flow {
        emit(Resource.Loading())
        try {
            val data = loadData()
            emit(Resource.Success(data.categories))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load categories"))
        }
    }

    override fun getVegetables(): Flow<Resource<List<Vegetable>>> = flow {
        emit(Resource.Loading())
        try {
            val data = loadData()
            emit(Resource.Success(data.vegetables))
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load vegetables"))
        }
    }

    override fun getVegetableById(id: String): Flow<Resource<Vegetable>> = flow {
        emit(Resource.Loading())
        try {
            val data = loadData()
            val vegetable = data.vegetables.find { it.id == id }
            if (vegetable != null) {
                emit(Resource.Success(vegetable))
            } else {
                emit(Resource.Error("Vegetable not found"))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Failed to load vegetable details"))
        }
    }

    override suspend fun toggleFavorite(vegetable: Vegetable) {
        if (favoriteDao.isFavorite(vegetable.id)) {
            favoriteDao.deleteFavoriteById(vegetable.id)
        } else {
            favoriteDao.insertFavorite(vegetable.toFavoriteItem())
        }
    }

    override suspend fun isFavorite(id: String): Boolean {
        return favoriteDao.isFavorite(id)
    }

    override fun getFavorites(): Flow<List<Vegetable>> {
        return favoriteDao.getAllFavorites().map { items ->
            items.map { it.toVegetable() }
        }
    }

    private fun loadData(): HomeDataResponse {
        val inputStream = context.assets.open("vegetables.json")
        val reader = InputStreamReader(inputStream)
        return gson.fromJson(reader, HomeDataResponse::class.java)
    }

    private data class HomeDataResponse(
        val categories: List<Category>,
        val vegetables: List<Vegetable>
    )
}

fun FavoriteItem.toVegetable() = Vegetable(
    id = id,
    name = name,
    description = description,
    price = price,
    originalPrice = originalPrice,
    unit = unit,
    categoryId = categoryId,
    imageUrl = imageUrl,
    rating = rating,
    deliveryTime = deliveryTime,
    isOrganic = isOrganic
)

fun Vegetable.toFavoriteItem() = FavoriteItem(
    id = id,
    name = name,
    price = price,
    originalPrice = originalPrice,
    unit = unit,
    imageUrl = imageUrl,
    categoryId = categoryId,
    isOrganic = isOrganic,
    rating = rating,
    deliveryTime = deliveryTime,
    description = description
)

