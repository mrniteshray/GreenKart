package com.greenkart.di

import com.google.gson.Gson
import com.greenkart.data.repository.HomeRepositoryImpl
import com.greenkart.domain.repository.HomeRepository
import com.greenkart.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeModule = module {
    single { Gson() }
    single<HomeRepository> { HomeRepositoryImpl(get(), get()) }
    viewModel { HomeViewModel(get()) }
}
