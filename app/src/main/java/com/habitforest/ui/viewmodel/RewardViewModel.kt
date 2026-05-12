package com.habitforest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitforest.data.model.Reward
import com.habitforest.data.model.RewardCatalogue
import com.habitforest.data.model.RewardDefinition
import com.habitforest.data.model.User
import com.habitforest.data.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

data class RewardUiState(
    val unlockedRewards: List<Reward> = emptyList(),
    val user: User? = null,
    val catalogue: List<RewardDefinition> = RewardCatalogue.ALL
)

@HiltViewModel
class RewardViewModel @Inject constructor(
    repo: HabitRepository
) : ViewModel() {

    val state: StateFlow<RewardUiState> = combine(
        repo.observeRewards(),
        repo.observeUser()
    ) { rewards, user ->
        RewardUiState(unlockedRewards = rewards, user = user)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), RewardUiState())
}
