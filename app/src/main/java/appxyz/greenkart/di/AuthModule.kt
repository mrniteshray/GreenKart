package appxyz.greenkart.di

import appxyz.greenkart.data.local.ProfileImageLocalDataStore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import appxyz.greenkart.data.repository.AuthRepositoryImpl
import appxyz.greenkart.domain.repository.AuthRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single { FirebaseFirestore.getInstance() }
    single { ProfileImageLocalDataStore(androidContext()) }
    
    single<AuthRepository> { AuthRepositoryImpl(get(), get()) }
    
    // Kotlin 2.0+ viewmodel builder syntax in Koin
    factory { appxyz.greenkart.presentation.auth.AuthViewModel(get(), get()) }
}

