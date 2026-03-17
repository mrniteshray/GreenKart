package com.greenkart.presentation.order

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.greenkart.domain.model.Order
import com.greenkart.domain.repository.OrderRepository
import com.greenkart.domain.util.Resource
import com.greenkart.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class OrderViewModel(
    private val orderRepository: OrderRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _state = mutableStateOf(OrderState())
    val state: State<OrderState> = _state

    init {
        getOrders()
    }

    private fun getOrders() {
        val currentUser = authViewModel.authState.value.user ?: return
        orderRepository.getOrders(currentUser.id).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    _state.value = state.value.copy(
                        orders = result.data ?: emptyList(),
                        isLoading = false
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

data class OrderState(
    val orders: List<Order> = emptyList(),
    val isLoading: Boolean = false,
    val error: String = ""
)
