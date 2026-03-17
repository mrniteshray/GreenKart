package com.greenkart.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenkart.domain.model.Category
import com.greenkart.domain.model.Vegetable
import com.greenkart.domain.repository.HomeRepository
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val vegetables: List<Vegetable> = emptyList(),
    val error: String? = null,
    val selectedCategoryId: String? = null
)

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            homeRepository.getCategories().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _homeState.value = _homeState.value.copy(
                            categories = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _homeState.value = _homeState.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _homeState.value = _homeState.value.copy(isLoading = true)
                    }
                }
            }

            homeRepository.getVegetables().collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _homeState.value = _homeState.value.copy(
                            vegetables = result.data ?: emptyList(),
                            isLoading = false
                        )
                    }
                    is Resource.Error -> {
                        _homeState.value = _homeState.value.copy(
                            error = result.message,
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _homeState.value = _homeState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun selectCategory(categoryId: String?) {
        _homeState.value = _homeState.value.copy(selectedCategoryId = categoryId)
    }
}
