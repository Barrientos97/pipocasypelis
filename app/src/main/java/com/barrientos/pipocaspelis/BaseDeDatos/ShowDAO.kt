package com.barrientos.pipocaspelis.BaseDeDatos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.barrientos.pipocaspelis.Modelos.show


@Dao
interface ShowDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun guardarList(shows:List<show>)

    @Query ("SELECT * FROM show")
    fun listPeliculas():List<show>
}