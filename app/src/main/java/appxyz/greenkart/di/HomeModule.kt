package appxyz.greenkart.di

import com.google.gson.Gson
import appxyz.greenkart.data.repository.HomeRepositoryImpl
import appxyz.greenkart.domain.repository.HomeRepository
import appxyz.greenkart.presentation.favorite.FavoriteViewModel
import appxyz.greenkart.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single { Gson() }
    single<HomeRepository> { HomeRepositoryImpl(get(), get(), get()) }
    viewModel { HomeViewModel(get()) }
    viewModel { FavoriteViewModel(get()) }
}

