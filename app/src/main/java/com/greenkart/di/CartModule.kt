package com.greenkart.di

import androidx.room.Room
import com.greenkart.data.local.GreenkartDatabase
import com.greenkart.data.repository.CartRepositoryImpl
import com.greenkart.data.repository.OrderRepositoryImpl
import com.greenkart.domain.repository.CartRepository
import com.greenkart.domain.repository.OrderRepository
import com.greenkart.presentation.cart.CartViewModel
import com.greenkart.presentation.detail.DetailViewModel
import com.greenkart.presentation.order.OrderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cartModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            GreenkartDatabase::class.java,
            "greenkart.db"
        ).fallbackToDestructiveMigration()
         .build()
    }
    single { get<GreenkartDatabase>().cartDao }
    single { get<GreenkartDatabase>().orderDao }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }

    viewModel { DetailViewModel(get(), get()) }
    viewModel { CartViewModel(get(), get(), get()) }
    viewModel { OrderViewModel(get(), get()) }
}
