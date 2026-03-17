package com.greenkart.data.repository

import android.content.Context
import com.google.gson.Gson
import com.greenkart.domain.model.Category
import com.greenkart.domain.model.Vegetable
import com.greenkart.domain.repository.HomeRepository
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InputStreamReader

class HomeRepositoryImpl(
    private val context: Context,
    private val gson: Gson
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
