package com.askein.gymtracker.ui.exercise.history

import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import java.time.LocalDate

class RecordCardioExerciseHistoryScreenKtTest {

    @Test
    fun shouldConvertCardioExerciseHistoryToRecordHistoryState() {
        val recordCardioHistoryState = CardioExerciseHistoryUiState(
            id = 1,
            date = LocalDate.now().minusDays(3),
            workoutHistoryId = 3,
            minutes = 10,
            seconds = 30,
            distance = 10.0,
            calories = 50
        ).toRecordCardioHistoryState(5, DistanceUnits.KILOMETERS)

        assertThat(
            recordCardioHistoryState, equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    minutesState = "10",
                    secondsState = "30",
                    caloriesState = "50",
                    distanceState = "10.0",
                    dateState = LocalDate.now().minusDays(3),
                    unitState = DistanceUnits.KILOMETERS
                )
            )
        )
    }

    @Test
    fun shouldConvertCardioExerciseHistoryToRecordHistoryStateWithNonKilometersUnit() {
        val recordCardioHistoryState = CardioExerciseHistoryUiState(
            id = 1,
            date = LocalDate.now().minusDays(3),
            workoutHistoryId = 3,
            minutes = null,
            seconds = null,
            distance = 10.0,
            calories = null
        ).toRecordCardioHistoryState(5, DistanceUnits.METERS)

        assertThat(
            recordCardioHistoryState, equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    minutesState = "",
                    secondsState = "",
                    caloriesState = "",
                    distanceState = "10000.0",
                    dateState = LocalDate.now().minusDays(3),
                    unitState = DistanceUnits.METERS
                )
            )
        )
    }

    @Test
    fun shouldConvertCardioExerciseHistoryToRecordHistoryStateWithNullDistance() {
        val recordCardioHistoryState = CardioExerciseHistoryUiState(
            id = 1,
            date = LocalDate.now().minusDays(3),
            workoutHistoryId = 3,
            minutes = null,
            seconds = null,
            distance = null,
            calories = 30
        ).toRecordCardioHistoryState(5, DistanceUnits.METERS)

        assertThat(
            recordCardioHistoryState, equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    minutesState = "",
                    secondsState = "",
                    caloriesState = "30",
                    distanceState = "",
                    dateState = LocalDate.now().minusDays(3),
                    unitState = DistanceUnits.METERS
                )
            )
        )
    }

    @Test
    fun shouldReturnCantSaveIfAllInvalidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(false))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(false))
    }

    @Test
    fun shouldReturnCantSaveIfEmptySecondsRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "10",
            secondsState = "",
            caloriesState = "",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(false))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(false))
    }

    @Test
    fun shouldReturnCantSaveIfTooLargeSecondsRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "10",
            secondsState = "80",
            caloriesState = "",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(false))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(false))
    }

    @Test
    fun shouldReturnCanSaveIfTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "10",
            secondsState = "50",
            caloriesState = "",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(false))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(true))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfDistanceValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "10",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(false))
        assertThat(cardioHistoryState.validDistance(), equalTo(true))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "5",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(true))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesAndTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "5",
            secondsState = "5",
            caloriesState = "5",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(true))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(true))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesAndDistanceValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "5",
            distanceState = "1",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(true))
        assertThat(cardioHistoryState.validDistance(), equalTo(true))
        assertThat(cardioHistoryState.validTime(), equalTo(false))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfDistanceAndTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "1",
            secondsState = "1",
            caloriesState = "5",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(true))
        assertThat(cardioHistoryState.validDistance(), equalTo(false))
        assertThat(cardioHistoryState.validTime(), equalTo(true))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfFullyValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "1",
            secondsState = "1",
            caloriesState = "5",
            distanceState = "1",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        )

        assertThat(cardioHistoryState.validCalories(), equalTo(true))
        assertThat(cardioHistoryState.validDistance(), equalTo(true))
        assertThat(cardioHistoryState.validTime(), equalTo(true))
        assertThat(cardioHistoryState.canSaveHistory(), equalTo(true))
    }

    @Test
    fun shouldConvertRecordHistoryStateToCardioExerciseHistory() {
        val cardioExerciseHistoryUiState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "10",
            secondsState = "30",
            caloriesState = "50",
            distanceState = "10.0",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.KILOMETERS
        ).toCardioExerciseHistoryUiState()

        assertThat(
            cardioExerciseHistoryUiState, equalTo(
                CardioExerciseHistoryUiState(
                    id = 1,
                    exerciseId = 5,
                    date = LocalDate.now().minusDays(3),
                    workoutHistoryId = 3,
                    minutes = 10,
                    seconds = 30,
                    distance = 10.0,
                    calories = 50
                )
            )
        )
    }

    @Test
    fun shouldConvertRecordHistoryStateToCardioExerciseHistoryWithNonKilometersUnit() {
        val cardioExerciseHistoryUiState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "10000.0",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        ).toCardioExerciseHistoryUiState()

        assertThat(
            cardioExerciseHistoryUiState, equalTo(
                CardioExerciseHistoryUiState(
                    id = 1,
                    exerciseId = 5,
                    date = LocalDate.now().minusDays(3),
                    workoutHistoryId = 3,
                    minutes = null,
                    seconds = null,
                    distance = 10.0,
                    calories = null
                )
            )
        )
    }

    @Test
    fun shouldConvertRecordHistoryStateToCardioExerciseHistoryWithNullDistance() {
        val cardioExerciseHistoryUiState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            minutesState = "",
            secondsState = "",
            caloriesState = "30",
            distanceState = "",
            dateState = LocalDate.now().minusDays(3),
            unitState = DistanceUnits.METERS
        ).toCardioExerciseHistoryUiState()

        assertThat(
            cardioExerciseHistoryUiState, equalTo(
                CardioExerciseHistoryUiState(
                    id = 1,
                    exerciseId = 5,
                    date = LocalDate.now().minusDays(3),
                    workoutHistoryId = 3,
                    minutes = null,
                    seconds = null,
                    distance = null,
                    calories = 30
                )
            )
        )
    }
}