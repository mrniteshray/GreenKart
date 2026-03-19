package appxyz.greenkart.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import appxyz.greenkart.domain.model.Vegetable
import appxyz.greenkart.domain.repository.HomeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class FavoriteViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val favoriteVegetables: StateFlow<List<Vegetable>> = homeRepository.getFavorites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

