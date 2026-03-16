package com.greenkart.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.greenkart.data.repository.AuthRepositoryImpl
import com.greenkart.domain.repository.AuthRepository
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    
    // Kotlin 2.0+ viewmodel builder syntax in Koin
    factory { com.greenkart.presentation.auth.AuthViewModel(get()) }
}
