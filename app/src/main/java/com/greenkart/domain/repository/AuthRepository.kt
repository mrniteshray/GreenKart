package com.greenkart.domain.repository

import com.greenkart.domain.model.User
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUser(): User?
    suspend fun login(email: String, password: String): Flow<Resource<User>>
    suspend fun signup(name: String, email: String, phone: String, address: String, password: String): Flow<Resource<User>>
    suspend fun logout()
    suspend fun fetchCurrentUserDetails(): Flow<Resource<User>>
    suspend fun updateUserDetails(phone: String, address: String): Flow<Resource<User>>
}
