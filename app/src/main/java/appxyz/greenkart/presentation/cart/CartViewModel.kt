package appxyz.greenkart.presentation.cart

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appxyz.greenkart.domain.model.CartItem
import appxyz.greenkart.domain.model.Order
import appxyz.greenkart.domain.repository.CartRepository
import appxyz.greenkart.domain.repository.OrderRepository
import appxyz.greenkart.domain.util.Resource
import appxyz.greenkart.presentation.auth.AuthViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val authViewModel: AuthViewModel
) : ViewModel() {

    private val _state = mutableStateOf(CartState())
    val state: State<CartState> = _state

    init {
        getCartItems()
    }

    private fun getCartItems() {
        cartRepository.getCartItems().onEach { items ->
            _state.value = state.value.copy(
                items = items,
                totalPrice = items.sumOf { it.price * it.quantity }
            )
        }.launchIn(viewModelScope)
    }

    fun addToCart(item: CartItem) {
        viewModelScope.launch {
            cartRepository.addToCart(item)
        }
    }

    fun updateQuantity(itemId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateQuantity(itemId, quantity)
        }
    }

    fun removeFromCart(item: CartItem) {
        viewModelScope.launch {
            cartRepository.removeFromCart(item)
        }
    }

    fun placeOrder() {
        val currentUser = authViewModel.authState.value.user ?: return
        val order = Order(
            userId = currentUser.id,
            items = state.value.items,
            totalPrice = state.value.totalPrice,
            deliveryAddress = currentUser.address ?: ""
        )

        viewModelScope.launch {
            _state.value = state.value.copy(isPlacingOrder = true)
            val result = orderRepository.placeOrder(order)
            when (result) {
                is Resource.Success -> {
                    cartRepository.clearCart()
                    _state.value = state.value.copy(
                        isPlacingOrder = false,
                        orderPlaced = true
                    )
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(
                        isPlacingOrder = false,
                        error = result.message ?: "Failed to place order"
                    )
                }
                else -> {}
            }
        }
    }
}

data class CartState(
    val items: List<CartItem> = emptyList(),
    val totalPrice: Double = 0.0,
    val isPlacingOrder: Boolean = false,
    val orderPlaced: Boolean = false,
    val error: String = ""
)

