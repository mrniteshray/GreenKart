package appxyz.greenkart.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appxyz.greenkart.domain.model.User
import appxyz.greenkart.domain.repository.AuthRepository
import appxyz.greenkart.domain.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AuthState(
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    init {
        checkCurrentUser()
    }

    private fun checkCurrentUser() {
        val currentUser = authRepository.getCurrentUser()
        if (currentUser != null) {
            _authState.value = _authState.value.copy(user = currentUser)
            
            // Fetch the full details asynchronously to grab Phone and Address
            viewModelScope.launch {
                authRepository.fetchCurrentUserDetails().collect { result ->
                    if (result is Resource.Success) {
                        _authState.value = _authState.value.copy(user = result.data, isLoading = false)
                    } else if (result is Resource.Error) {
                        _authState.value = _authState.value.copy(error = result.message, isLoading = false)
                    } else if (result is Resource.Loading) {
                        _authState.value = _authState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _authState.value = AuthState(error = "Please fill in all fields")
            return
        }
        if (!isValidEmail(email)) {
            _authState.value = AuthState(error = "Please enter a valid email address")
            return
        }

        viewModelScope.launch {
            authRepository.login(email, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.value = AuthState(user = result.data)
                    }
                    is Resource.Error -> {
                        _authState.value = AuthState(error = result.message)
                    }
                    is Resource.Loading -> {
                        _authState.value = AuthState(isLoading = true)
                    }
                }
            }
        }
    }

    fun signup(name: String, email: String, phone: String, address: String, password: String) {
        if (name.isBlank() || email.isBlank() || phone.isBlank() || address.isBlank() || password.isBlank()) {
            _authState.value = AuthState(error = "Please fill in all fields")
            return
        }
        if (!isValidEmail(email)) {
            _authState.value = AuthState(error = "Please enter a valid email address")
            return
        }
        if (password.length < 6) {
            _authState.value = AuthState(error = "Password must be at least 6 characters long")
            return
        }
        if (phone.length < 10) {
            _authState.value = AuthState(error = "Please enter a valid phone number")
            return
        }

        viewModelScope.launch {
            authRepository.signup(name, email, phone, address, password).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.value = AuthState(user = result.data)
                    }
                    is Resource.Error -> {
                        _authState.value = AuthState(error = result.message)
                    }
                    is Resource.Loading -> {
                        _authState.value = AuthState(isLoading = true)
                    }
                }
            }
        }
    }

    fun updateProfile(phone: String, address: String) {
        if (phone.isBlank() || address.isBlank()) {
            _authState.value = _authState.value.copy(error = "Please fill in all fields")
            return
        }

        viewModelScope.launch {
            authRepository.updateUserDetails(phone, address).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.value = _authState.value.copy(user = result.data, isLoading = false, error = null)
                    }
                    is Resource.Error -> {
                        _authState.value = _authState.value.copy(error = result.message, isLoading = false)
                    }
                    is Resource.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun updateAddressFromHome(address: String) {
        if (address.isBlank()) {
            _authState.value = _authState.value.copy(error = "Address cannot be empty")
            return
        }

        val currentPhone = _authState.value.user?.phone ?: ""

        viewModelScope.launch {
            authRepository.updateUserDetails(currentPhone, address).collect { result ->
                when (result) {
                    is Resource.Success -> {
                        _authState.value = _authState.value.copy(user = result.data, isLoading = false, error = null)
                    }
                    is Resource.Error -> {
                        _authState.value = _authState.value.copy(error = result.message, isLoading = false)
                    }
                    is Resource.Loading -> {
                        _authState.value = _authState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }

    fun clearError() {
        _authState.value = _authState.value.copy(error = null)
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _authState.value = AuthState()
        }
    }
}

