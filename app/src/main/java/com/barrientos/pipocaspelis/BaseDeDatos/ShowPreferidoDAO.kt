package com.barrientos.pipocaspelis.BaseDeDatos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.barrientos.pipocaspelis.Modelos.ShowPrefered

@Dao
interface ShowPreferidoDAO {
    @Query ("SELECT * FROM preferidos WHERE showId = :showId")
    fun get (showId : Long) : ShowPrefered

     @Insert
     fun guardar(preferidos: ShowPrefered)

     @Update
     fun actualizar(preferidos: ShowPrefered)
}