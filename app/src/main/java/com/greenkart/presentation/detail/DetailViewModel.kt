package com.greenkart.presentation.detail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenkart.domain.model.CartItem
import com.greenkart.domain.model.Vegetable
import com.greenkart.domain.repository.CartRepository
import com.greenkart.domain.repository.HomeRepository
import com.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DetailViewModel(
    private val homeRepository: HomeRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _state = mutableStateOf<DetailState>(DetailState())
    val state: State<DetailState> = _state

    fun getVegetable(id: String) {
        viewModelScope.launch {
            homeRepository.getVegetableById(id).onEach { result ->
                when (result) {
                    is Resource.Success -> {
                        val isFav = homeRepository.isFavorite(id)
                        _state.value = state.value.copy(
                            vegetable = result.data,
                            isLoading = false,
                            isFavorite = isFav
                        )
                    }
                    is Resource.Error -> {
                        _state.value = state.value.copy(
                            error = result.message ?: "An unexpected error occurred",
                            isLoading = false
                        )
                    }
                    is Resource.Loading -> {
                        _state.value = state.value.copy(isLoading = true)
                    }
                }
            }.launchIn(viewModelScope)
        }
    }

    fun toggleFavorite() {
        state.value.vegetable?.let { vegetable ->
            viewModelScope.launch {
                homeRepository.toggleFavorite(vegetable)
                _state.value = state.value.copy(isFavorite = !state.value.isFavorite)
            }
        }
    }

    fun addToCart(vegetable: Vegetable) {
        viewModelScope.launch {
            cartRepository.addToCart(
                CartItem(
                    id = vegetable.id,
                    name = vegetable.name,
                    price = vegetable.price,
                    unit = vegetable.unit,
                    imageUrl = vegetable.imageUrl,
                    quantity = 1
                )
            )
        }
    }
}

data class DetailState(
    val vegetable: Vegetable? = null,
    val isLoading: Boolean = false,
    val error: String = "",
    val isFavorite: Boolean = false
)
