package com.example.gymtracker.data.exercise

import kotlinx.coroutines.flow.Flow

class OfflineExerciseRepository(private val exerciseDao: ExerciseDao) : ExerciseRepository {
    override fun getAllExercisesStream(): Flow<List<Exercise>> = exerciseDao.getAllExercises()

    override fun getExerciseStream(id: Int): Flow<Exercise?> = exerciseDao.getExercise(id)

    override fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise>> =
        exerciseDao.getAllExercisesByMuscleGroup(muscleGroup)

    override fun getAllMuscleGroupsStream(): Flow<List<String>> = exerciseDao.getAllMuscleGroups()
    override fun getAllExerciseNames(): Flow<List<String>> = exerciseDao.getAllExerciseNames()

    override suspend fun insertExercise(exercise: Exercise) = exerciseDao.insert(exercise)

    override suspend fun deleteExercise(exercise: Exercise) = exerciseDao.delete(exercise)

    override suspend fun updateExercise(exercise: Exercise) = exerciseDao.update(exercise)
}
