//package com.example.gymtracker.ui.exercise.details
//
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.gymtracker.data.exercise.Exercise
//import com.example.gymtracker.data.exercise.ExerciseRepository
//import com.example.gymtracker.data.history.HistoryRepository
//import com.example.gymtracker.ui.exercise.ExerciseDetailsUiState
//import com.example.gymtracker.ui.exercise.toExerciseDetailsUiState
//import com.example.gymtracker.ui.history.toExerciseHistoryUiState
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.flow.map
//import kotlinx.coroutines.flow.stateIn
//
//class ExerciseDetailsViewModel(
//    exerciseRepository: ExerciseRepository,
//    private val historyRepository: HistoryRepository,
//    savedStateHandle: SavedStateHandle
//) : ViewModel() {
//
//    private val exerciseId: Int = checkNotNull(savedStateHandle["exerciseId"])
//
//    companion object {
//        private const val TIMEOUT_MILLIS = 5_000L
//    }
//
//    val uiState: StateFlow<ExerciseDetailsUiState> =
//        exerciseRepository.getExerciseStream(exerciseId)
//            .map { exercise -> exerciseToExerciseDetails(exercise, historyRepository) }
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = ExerciseDetailsUiState()
//            )
//
//    private suspend fun exerciseToExerciseDetails(
//        exercise: Exercise?,
//        historyRepository: HistoryRepository
//    ) : ExerciseDetailsUiState {
//        val uiState = exercise?.toExerciseDetailsUiState() ?: ExerciseDetailsUiState()
//        val history = historyRepository.getFullExerciseHistoryStream(uiState.id)
//            .first()
//            ?.map { history -> history.toExerciseHistoryUiState() }
//        uiState.history = history
//        return uiState
//    }
//}
