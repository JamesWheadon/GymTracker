package com.example.gymtracker.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymtracker.data.history.HistoryRepository
import com.example.gymtracker.ui.exercise.ExerciseUiState
import com.example.gymtracker.ui.exercise.toExerciseUiState
import com.example.gymtracker.ui.workout.WorkoutUiState
import com.example.gymtracker.ui.workout.toWorkoutUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class OverallHistoryViewModel(
    historyRepository: HistoryRepository
) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val selectedDate = MutableStateFlow(0L)

    val datesUiState: StateFlow<List<LocalDate>> = historyRepository.getDatesStream().map { dates ->
        dates.map { date -> LocalDate.ofEpochDay(date) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        initialValue = listOf()
    )

    val workoutsOnDateUiState: StateFlow<List<WorkoutUiState>> =
        selectedDate.flatMapLatest { date ->
            historyRepository.getWorkoutsForDate(date).map { workouts ->
                workouts.map { workout -> workout.toWorkoutUiState() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    val exercisesOnDateUiState: StateFlow<List<ExerciseUiState>> =
        selectedDate.flatMapLatest { date ->
            historyRepository.getExercisesForDate(date).map { exercises ->
                exercises.map { exercise -> exercise.toExerciseUiState() }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = listOf()
        )

    fun selectDate(date: LocalDate) {
        viewModelScope.launch {
            selectedDate.emit(date.toEpochDay())
        }
    }
}
