package com.askein.gymtracker.ui.exercise.history.state.record

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.askein.gymtracker.enums.WeightUnits
import com.askein.gymtracker.ui.exercise.history.state.WeightsExerciseHistoryUiState
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.experimental.runners.Enclosed
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.time.LocalDate

@RunWith(Enclosed::class)
class RecordWeightsHistoryStateKtTest {

    @RunWith(value = Parameterized::class)
    class RecordWeightsHistoryStateUpdateStateTests(
        private val setsState: String,
        private val repsState: SnapshotStateList<String>,
        private val minutesState: SnapshotStateList<String>,
        private val secondsState: SnapshotStateList<String>,
        private val weightsState: SnapshotStateList<String>,
        private val recordReps: Boolean,
        private val recordWeights: Boolean,
        private val expectedReps: List<String>,
        private val expectedMinutes: List<String>,
        private val expectedSeconds: List<String>,
        private val expectedWeights: List<String>
    ) {
        @Test
        fun testRecordWeightsHistoryStateUpdateState() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = setsState,
                repsState = repsState,
                minutesState = minutesState,
                secondsState = secondsState,
                weightsState = weightsState,
                unitState = WeightUnits.KILOGRAMS,
                recordReps = recordReps,
                recordWeight = recordWeights
            )

            historyState.updateState()

            assertThat(historyState.repsState.toList(), equalTo(expectedReps))
            assertThat(historyState.minutesState.toList(), equalTo(expectedMinutes))
            assertThat(historyState.secondsState.toList(), equalTo(expectedSeconds))
            assertThat(historyState.weightsState.toList(), equalTo(expectedWeights))
        }

        companion object {
            @JvmStatic
            @Parameterized.Parameters(name = "{index}: recordReps: {5}, recordWeights: {6}")
            fun data(): List<Array<Any?>> {
                return listOf(
                    arrayOf(
                        "2",
                        mutableStateListOf<String>(),
                        mutableStateListOf("3"),
                        mutableStateListOf("3"),
                        mutableStateListOf<String>(),
                        true,
                        false,
                        listOf("0", "0"),
                        listOf<String>(),
                        listOf<String>(),
                        listOf<String>()
                    ),
                    arrayOf(
                        "3",
                        mutableStateListOf<String>(),
                        mutableStateListOf("3"),
                        mutableStateListOf("3"),
                        mutableStateListOf<String>(),
                        true,
                        true,
                        listOf("0", "0", "0"),
                        listOf<String>(),
                        listOf<String>(),
                        listOf("0.0", "0.0", "0.0")
                    ),
                    arrayOf(
                        "1",
                        mutableStateListOf("5", "7"),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        true,
                        false,
                        listOf("5"),
                        listOf<String>(),
                        listOf<String>(),
                        listOf<String>()
                    ),
                    arrayOf(
                        "1",
                        mutableStateListOf("5", "7"),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        mutableStateListOf("10.5", "14.6"),
                        true,
                        true,
                        listOf("5"),
                        listOf<String>(),
                        listOf<String>(),
                        listOf("10.5")
                    ),
                    arrayOf(
                        "2",
                        mutableStateListOf("3"),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        false,
                        false,
                        listOf<String>(),
                        listOf("0", "0"),
                        listOf("0", "0"),
                        listOf<String>()
                    ),
                    arrayOf(
                        "3",
                        mutableStateListOf("3"),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        mutableStateListOf<String>(),
                        false,
                        true,
                        listOf<String>(),
                        listOf("0", "0", "0"),
                        listOf("0", "0", "0"),
                        listOf("0.0", "0.0", "0.0")
                    ),
                    arrayOf(
                        "1",
                        mutableStateListOf<String>(),
                        mutableStateListOf("5", "7"),
                        mutableStateListOf("8", "12"),
                        mutableStateListOf<String>(),
                        false,
                        false,
                        listOf<String>(),
                        listOf("5"),
                        listOf("8"),
                        listOf<String>()
                    ),
                    arrayOf(
                        "1",
                        mutableStateListOf<String>(),
                        mutableStateListOf("5", "7"),
                        mutableStateListOf("8", "12"),
                        mutableStateListOf("10.5", "14.6"),
                        false,
                        true,
                        listOf<String>(),
                        listOf("5"),
                        listOf("8"),
                        listOf("10.5")
                    )
                )
            }
        }
    }

    class RecordWeightsHistoryStateIsValidTests {
        @Test
        fun shouldReturnStateIsValidWhenSetsAndRepsCorrect() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("1", "3"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = false
            )

            assertThat(historyState.isValid(), equalTo(true))
        }

        @Test
        fun shouldReturnStateIsValidWhenSetsRepsAndWeightsCorrect() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("1", "3"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("0.0", "5.1"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(true))
        }

        @Test
        fun shouldReturnStateIsValidWhenSetsAndTimesCorrect() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("6", "50"),
                secondsState = mutableStateListOf("5", "30"),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = false
            )

            assertThat(historyState.isValid(), equalTo(true))
        }

        @Test
        fun shouldReturnStateIsValidWhenSetsTimeAndWeightsCorrect() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("6", "50"),
                secondsState = mutableStateListOf("5", "30"),
                weightsState = mutableStateListOf("0.0", "5.1"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(true))
        }

        @Test
        fun shouldReturnStateInvalidWhenSetsEmpty() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenSetsZero() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "0",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenWeightsContainsEmpty() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("3", "6"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("1.0", ""),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenRepsContainsEmpty() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("3", ""),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("1.0", "2.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenMinutesContainsEmpty() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("4", ""),
                secondsState = mutableStateListOf("6", "4"),
                weightsState = mutableStateListOf("1.0", "2.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenSecondsContainsEmpty() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("4", "7"),
                secondsState = mutableStateListOf("6", ""),
                weightsState = mutableStateListOf("1.0", "2.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }

        @Test
        fun shouldReturnStateInvalidWhenSecondsContainsValueGreaterThan59() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("4", "7"),
                secondsState = mutableStateListOf("6", "60"),
                weightsState = mutableStateListOf("1.0", "2.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = true
            )

            assertThat(historyState.isValid(), equalTo(false))
        }
    }

    class RecordWeightsHistoryStateAllSetsSameAsFirstTests {
        @Test
        fun shouldSetAllSetsToSameAsTheFirstForRepsNoWeight() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("1", "3"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = false
            )

            historyState.allSetsSameAsFirst()

            assertThat(historyState.repsState.toList(), equalTo(listOf("1", "1")))
        }

        @Test
        fun shouldSetAllSetsToSameAsTheFirstForRepsAndWeight() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("1", "3"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("3.5", "1.5"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            historyState.allSetsSameAsFirst()

            assertThat(historyState.repsState.toList(), equalTo(listOf("1", "1")))
            assertThat(historyState.weightsState.toList(), equalTo(listOf("3.5", "3.5")))
        }

        @Test
        fun shouldSetAllSetsToSameAsTheFirstForTimeAndWeight() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("5", "60"),
                secondsState = mutableStateListOf("20", "45"),
                weightsState = mutableStateListOf("3.5", "1.5"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = true
            )

            historyState.allSetsSameAsFirst()

            assertThat(historyState.minutesState.toList(), equalTo(listOf("5", "5")))
            assertThat(historyState.secondsState.toList(), equalTo(listOf("20", "20")))
            assertThat(historyState.weightsState.toList(), equalTo(listOf("3.5", "3.5")))
        }
    }

    class RecordWeightsHistoryStateAllSetsEqualTests {
        @Test
        fun shouldReturnTrueWhenAllRepsEqualAndNoWeights() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("5", "5"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf(),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = false
            )

            assertThat(historyState.allSetsEqual(), equalTo(true))
        }

        @Test
        fun shouldReturnTrueWhenAllRepsAndWeightsEqual() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("5", "5"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("1.0", "1.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = true,
                recordWeight = true
            )

            assertThat(historyState.allSetsEqual(), equalTo(true))
        }

        @Test
        fun shouldReturnTrueWhenAllTimeAndWeightsEqual() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("5", "5"),
                secondsState = mutableStateListOf("5", "5"),
                weightsState = mutableStateListOf("1.0", "1.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = true
            )

            assertThat(historyState.allSetsEqual(), equalTo(true))
        }
    }

    class RecordWeightsHistoryStateUiStateConversionTests {
        @Test
        fun shouldReturnUiStateWithReps() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf("3", "4"),
                minutesState = mutableStateListOf(),
                secondsState = mutableStateListOf(),
                weightsState = mutableStateListOf("10.0", "0.0"),
                unitState = WeightUnits.POUNDS,
                recordReps = true,
                recordWeight = false
            )
            val uiState = WeightsExerciseHistoryUiState(
                id = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                date = LocalDate.now(),
                rest = null,
                sets = 2,
                reps = listOf(3, 4),
                seconds = null,
                weight = listOf(4.54, 0.0)
            )

            assertThat(historyState.toHistoryUiState(), equalTo(uiState))
        }

        @Test
        fun shouldReturnUiStateWithSeconds() {
            val historyState = RecordWeightsHistoryState(
                historyId = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                dateState = LocalDate.now(),
                rest = null,
                setsState = "2",
                repsState = mutableStateListOf(),
                minutesState = mutableStateListOf("5", "7"),
                secondsState = mutableStateListOf("0", "30"),
                weightsState = mutableStateListOf("4.0", "5.0"),
                unitState = WeightUnits.KILOGRAMS,
                recordReps = false,
                recordWeight = false
            )
            val uiState = WeightsExerciseHistoryUiState(
                id = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                date = LocalDate.now(),
                rest = null,
                sets = 2,
                reps = null,
                seconds = listOf(300, 450),
                weight = listOf(4.0, 5.0)
            )

            assertThat(historyState.toHistoryUiState(), equalTo(uiState))
        }

        @Test
        fun shouldReturnHistoryStateWithReps() {
            val uiState = WeightsExerciseHistoryUiState(
                id = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                date = LocalDate.now(),
                rest = null,
                sets = 2,
                reps = listOf(3, 4),
                seconds = listOf(),
                weight = listOf(4.54, 0.0)
            )

            val actual = uiState.toRecordWeightsHistoryState(0, false, WeightUnits.POUNDS)
            assertThat(actual.repsState.toList(), equalTo(listOf("3", "4")))
            assertThat(actual.minutesState.toList(), equalTo(listOf()))
            assertThat(actual.secondsState.toList(), equalTo(listOf()))
            assertThat(actual.weightsState.toList(), equalTo(listOf("10.01", "0.0")))
        }

        @Test
        fun shouldReturnHistoryStateWithMinutsAndSeconds() {
            val uiState = WeightsExerciseHistoryUiState(
                id = 0,
                exerciseId = 0,
                workoutHistoryId = null,
                date = LocalDate.now(),
                rest = null,
                sets = 2,
                reps = listOf(),
                seconds = listOf(300, 450),
                weight = listOf(4.0, 5.0)
            )

            val actual = uiState.toRecordWeightsHistoryState(0, false, WeightUnits.KILOGRAMS)
            assertThat(actual.repsState.toList(), equalTo(listOf()))
            assertThat(actual.minutesState.toList(), equalTo(listOf("5", "7")))
            assertThat(actual.secondsState.toList(), equalTo(listOf("0", "30")))
            assertThat(actual.weightsState.toList(), equalTo(listOf("4.0", "5.0")))
        }
    }
}
