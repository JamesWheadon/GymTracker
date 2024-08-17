package com.askein.gymtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askein.gymtracker.data.history.HistoryRepository
import com.askein.gymtracker.ui.exercise.toExerciseUiState
import com.askein.gymtracker.ui.workout.toWorkoutUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class OverallHistoryViewModel(
    historyRepository: HistoryRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    private val selectedDate: MutableStateFlow<Long?> = MutableStateFlow(null)

    val datesUiState: StateFlow<List<LocalDate>> = historyRepository.getDatesStream().map { dates ->
        dates.map { date -> LocalDate.ofEpochDay(date) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyUiState: StateFlow<HistoryUiState> = selectedDate.flatMapLatest { date ->
        if (date != null) {
            combine(
                historyRepository.getWorkoutsForDate(date).map { workouts ->
                    workouts.map { workout -> workout.toWorkoutUiState() }
                },
                historyRepository.getExercisesForDate(date).map { exercises ->
                    exercises.map { exercise -> exercise.toExerciseUiState() }
                }
            ) { workouts, exercises ->
                HistoryUiState(
                    workouts = workouts,
                    exercises = exercises,
                    date = LocalDate.ofEpochDay(date)
                )
            }
        } else {
            flowOf(HistoryUiState())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = HistoryUiState()
    )

    fun selectDate(date: LocalDate?) {
        viewModelScope.launch {
            selectedDate.emit(date?.toEpochDay())
        }
    }
}
