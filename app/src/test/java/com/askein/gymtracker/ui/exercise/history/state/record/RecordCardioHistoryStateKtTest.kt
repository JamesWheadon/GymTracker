package com.askein.gymtracker.ui.exercise.history.state.record

import com.askein.gymtracker.enums.DistanceUnits
import com.askein.gymtracker.ui.exercise.history.state.CardioExerciseHistoryUiState
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Test
import java.time.LocalDate

class RecordCardioHistoryStateKtTest {

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

        MatcherAssert.assertThat(
            recordCardioHistoryState, CoreMatchers.equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    dateState = LocalDate.now().minusDays(3),
                    minutesState = "10",
                    secondsState = "30",
                    caloriesState = "50",
                    distanceState = "10.0",
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

        MatcherAssert.assertThat(
            recordCardioHistoryState, CoreMatchers.equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    dateState = LocalDate.now().minusDays(3),
                    minutesState = "",
                    secondsState = "",
                    caloriesState = "",
                    distanceState = "10000.0",
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

        MatcherAssert.assertThat(
            recordCardioHistoryState, CoreMatchers.equalTo(
                RecordCardioHistoryState(
                    historyId = 1,
                    exerciseId = 5,
                    workoutHistoryId = 3,
                    dateState = LocalDate.now().minusDays(3),
                    minutesState = "",
                    secondsState = "",
                    caloriesState = "30",
                    distanceState = "",
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
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(false))
    }

    @Test
    fun shouldReturnCantSaveIfEmptySecondsRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "10",
            secondsState = "",
            caloriesState = "",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(false))
    }

    @Test
    fun shouldReturnCantSaveIfTooLargeSecondsRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "10",
            secondsState = "80",
            caloriesState = "",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(false))
    }

    @Test
    fun shouldReturnCanSaveIfTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "10",
            secondsState = "50",
            caloriesState = "",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfDistanceValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "10",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "5",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesAndTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "5",
            secondsState = "5",
            caloriesState = "5",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfCaloriesAndDistanceValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "5",
            distanceState = "1",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfDistanceAndTimeValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "1",
            secondsState = "1",
            caloriesState = "5",
            distanceState = "",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(false))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldReturnCanSaveIfFullyValidRecordCardioState() {
        val cardioHistoryState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "1",
            secondsState = "1",
            caloriesState = "5",
            distanceState = "1",
            unitState = DistanceUnits.METERS
        )

        MatcherAssert.assertThat(cardioHistoryState.validCalories(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validDistance(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.validTime(), CoreMatchers.equalTo(true))
        MatcherAssert.assertThat(cardioHistoryState.isValid(), CoreMatchers.equalTo(true))
    }

    @Test
    fun shouldConvertRecordHistoryStateToCardioExerciseHistory() {
        val cardioExerciseHistoryUiState = RecordCardioHistoryState(
            historyId = 1,
            exerciseId = 5,
            workoutHistoryId = 3,
            dateState = LocalDate.now().minusDays(3),
            minutesState = "10",
            secondsState = "30",
            caloriesState = "50",
            distanceState = "10.0",
            unitState = DistanceUnits.KILOMETERS
        ).toHistoryUiState()

        MatcherAssert.assertThat(
            cardioExerciseHistoryUiState, CoreMatchers.equalTo(
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
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "",
            distanceState = "10000.0",
            unitState = DistanceUnits.METERS
        ).toHistoryUiState()

        MatcherAssert.assertThat(
            cardioExerciseHistoryUiState, CoreMatchers.equalTo(
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
            dateState = LocalDate.now().minusDays(3),
            minutesState = "",
            secondsState = "",
            caloriesState = "30",
            distanceState = "",
            unitState = DistanceUnits.METERS
        ).toHistoryUiState()

        MatcherAssert.assertThat(
            cardioExerciseHistoryUiState, CoreMatchers.equalTo(
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