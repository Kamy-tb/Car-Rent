package com.example.tp4_1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface TrajetDao {

    @Insert
    fun addTrajets(vararg trajet: Trajet)

    @Query("select * from trajets")
    fun getTrajets():List<Trajet>

    @Update
    fun updateTrajet(trajet: Trajet)

    @Delete
    fun deleteTrajet(trajet: Trajet)
}