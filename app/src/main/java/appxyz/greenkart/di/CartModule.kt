package appxyz.greenkart.di

import androidx.room.Room
import appxyz.greenkart.data.local.GreenkartDatabase
import appxyz.greenkart.data.repository.CartRepositoryImpl
import appxyz.greenkart.data.repository.OrderRepositoryImpl
import appxyz.greenkart.domain.repository.CartRepository
import appxyz.greenkart.domain.repository.OrderRepository
import appxyz.greenkart.presentation.cart.CartViewModel
import appxyz.greenkart.presentation.detail.DetailViewModel
import appxyz.greenkart.presentation.order.OrderViewModel
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
    single { get<GreenkartDatabase>().favoriteDao }
    single<CartRepository> { CartRepositoryImpl(get()) }
    single<OrderRepository> { OrderRepositoryImpl(get()) }

    viewModel { DetailViewModel(get(), get()) }
    viewModel { CartViewModel(get(), get(), get()) }
    viewModel { OrderViewModel(get(), get()) }
}

