package com.greenkart.domain.repository

import com.greenkart.domain.model.Category
import com.greenkart.domain.model.Vegetable
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getCategories(): Flow<Resource<List<Category>>>
    fun getVegetables(): Flow<Resource<List<Vegetable>>>
    fun getVegetableById(id: String): Flow<Resource<Vegetable>>
    suspend fun toggleFavorite(vegetable: Vegetable)
    suspend fun isFavorite(id: String): Boolean
    fun getFavorites(): Flow<List<Vegetable>>
}
