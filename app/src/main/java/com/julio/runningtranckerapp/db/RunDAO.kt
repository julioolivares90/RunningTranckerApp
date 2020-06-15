package com.julio.runningtranckerapp.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface RunDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run : Run)

    @Delete
    suspend fun deleteRun(run: Run)

    @Query("SELECT * FROM running_table ORDER BY timestamp desc ")
    fun getAllRunsSortedByDate(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY avgSpeedInKMH desc ")
    fun getAllRunsSortedByAvgSpeedInKmh(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY distanceInMeters desc ")
    fun getAllRunsSortedByDistanceInMeters(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY timeInMillis desc ")
    fun getAllRunsSortedByTimeInMillis(): LiveData<List<Run>>

    @Query("SELECT * FROM running_table ORDER BY caloriesBurned desc ")
    fun getAllRunsSortedByCaloriesBurned(): LiveData<List<Run>>


    @Query("select sum(timeInMillis) from running_table")
    fun getTotalTimeInMillis(): LiveData<Long>

    @Query("select sum(caloriesBurned) from running_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("select sum(distanceInMeters) from running_table")
    fun getTotalDistanceInMeters(): LiveData<Long>

    @Query("select avg(avgSpeedInKMH) from running_table")
    fun getTotalAVgSpeed(): LiveData<Float>


}