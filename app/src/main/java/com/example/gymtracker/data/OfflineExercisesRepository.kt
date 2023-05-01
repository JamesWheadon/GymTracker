package com.example.gymtracker.data

import kotlinx.coroutines.flow.Flow

class OfflineExercisesRepository(private val exerciseDao: ExerciseDao) : ExercisesRepository {

    override fun getExerciseStream(id: Int): Flow<Exercise?> = exerciseDao.getExercise(id)

    override fun getAllExercisesStream(): Flow<List<Exercise?>> = exerciseDao.getAllExercises()

    override fun getAllMuscleGroupExercisesStream(muscleGroup: String): Flow<List<Exercise?>> =
        exerciseDao.getAllExercisesByMuscleGroup(muscleGroup)

    override suspend fun insertExercise(exercise: Exercise) = exerciseDao.insert(exercise)

    override suspend fun deleteExercise(exercise: Exercise) = exerciseDao.delete(exercise)

    override suspend fun updateExercise(exercise: Exercise) = exerciseDao.delete(exercise)
}
