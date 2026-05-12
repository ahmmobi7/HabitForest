package com.habitforest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.habitforest.data.model.Habit
import com.habitforest.data.model.User
import com.habitforest.data.repository.HabitRepository
import com.habitforest.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import javax.inject.Inject

data class HomeUiState(
    val habits: List<Habit> = emptyList(),
    val user: User? = null,
    val todayLogs: Map<String, String> = emptyMap(),   // habitId → "YES"/"NO"/""
    val insights: List<Insight> = emptyList(),
    val isLoading: Boolean = false,
    val rewardEvent: RewardEvent? = null,
    val toast: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: HabitRepository,
    private val getHabits: GetHabitsUseCase,
    private val logHabit: LogHabitUseCase,
    private val addHabit: AddHabitUseCase,
    private val insightUseCase: InsightUseCase,
    private val rewardEngine: RewardEngine
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state = _state.asStateFlow()

    init {
        observeData()
        loadInsights()
    }

    private fun observeData() {
        viewModelScope.launch {
            combine(getHabits(), repo.observeUser()) { habits, user ->
                Pair(habits, user)
            }.collect { (habits, user) ->
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault()).toString()
                // Build today's log map from Room for each habit
                val logsMap = habits.associate { h ->
                    h.id to (repo.getLogForDate(h.id, today)?.status ?: "")
                }
                _state.update { it.copy(habits = habits, user = user, todayLogs = logsMap) }
            }
        }
    }

    private fun loadInsights() {
        viewModelScope.launch {
            val list = insightUseCase()
            _state.update { it.copy(insights = list) }
        }
    }

    fun logHabitToday(habit: Habit, completed: Boolean) {
        viewModelScope.launch {
            logHabit(habit, completed)
                .onSuccess { result ->
                    // Optimistically update map
                    val updated = _state.value.todayLogs.toMutableMap()
                    updated[habit.id] = if (completed) "YES" else "NO"
                    _state.update { it.copy(todayLogs = updated) }

                    if (completed) {
                        _state.update { it.copy(toast = "+${result.xpEarned} XP ⚡${if (result.justGrew) "  🌱→${result.updatedHabit.stage.emoji}" else ""}") }
                        // Check rewards
                        _state.value.user?.let { user ->
                            val reward = rewardEngine.check(user)
                            if (reward != null) _state.update { it.copy(rewardEvent = reward) }
                        }
                    } else {
                        _state.update { it.copy(toast = "Logged — no worries, keep going! 💪") }
                    }
                }
                .onFailure { e ->
                    _state.update { it.copy(toast = e.message ?: "Error logging habit") }
                }
        }
    }

    fun addNewHabit(name: String, icon: String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            addHabit(name, icon)
                .onSuccess { _state.update { it.copy(isLoading = false, toast = "Habit planted! 🌱") } }
                .onFailure { e -> _state.update { it.copy(isLoading = false, toast = e.message ?: "Failed to add habit") } }
        }
    }

    fun deleteHabit(habit: Habit) {
        viewModelScope.launch {
            repo.deleteHabit(habit)
        }
    }

    fun dismissReward() = _state.update { it.copy(rewardEvent = null) }
    fun dismissToast()  = _state.update { it.copy(toast = null) }
}
