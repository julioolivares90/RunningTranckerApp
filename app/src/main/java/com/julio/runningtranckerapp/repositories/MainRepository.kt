package com.julio.runningtranckerapp.repositories

import com.julio.runningtranckerapp.db.Run
import com.julio.runningtranckerapp.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
    val runDAO: RunDAO
) {
    suspend fun insertRun(run: Run) = runDAO.insertRun(run)

    suspend fun deleteRun(run: Run) = runDAO.deleteRun(run)

    fun getAllRunsSortedByDate() = runDAO.getAllRunsSortedByDate()
    fun getAllRunsSortedByDistance() = runDAO.getAllRunsSortedByDistanceInMeters()
    fun getAllRunsSortedByTimeInMillis() = runDAO.getAllRunsSortedByTimeInMillis()
    fun getAllRunsSortedByAvgSpeed() = runDAO.getAllRunsSortedByAvgSpeedInKmh()
    fun getAllRunsSortedByBurned() = runDAO.getAllRunsSortedByCaloriesBurned()

    fun getTotalAvgSpeed() = runDAO.getTotalAVgSpeed()

    fun getTotalDistance() = runDAO.getTotalDistanceInMeters()

    fun getTotalCaloriesBurned() = runDAO.getTotalCaloriesBurned()

    fun getTotalTimeInMillis() = runDAO.getTotalTimeInMillis()


}