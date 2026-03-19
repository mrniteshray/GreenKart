package appxyz.greenkart.domain.repository

import appxyz.greenkart.domain.model.Category
import appxyz.greenkart.domain.model.Vegetable
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getVegetables(): Flow<Resource<List<Vegetable>>>
    fun getVegetableById(id: String): Flow<Resource<Vegetable>>
    suspend fun toggleFavorite(vegetable: Vegetable)
    suspend fun isFavorite(id: String): Boolean
    fun getFavorites(): Flow<List<Vegetable>>
}

