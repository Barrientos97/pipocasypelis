package com.barrientos.pipocaspelis.BaseDeDatos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.barrientos.pipocaspelis.Modelos.ShowImage


@Dao
interface ImagenesDAO {
    @Insert
    fun guardar(image: ShowImage)

    @Insert
    fun guardar (image: List <ShowImage>)

    @Query("SELECT * FROM image")
    fun lecturaSimple():ShowImage

    @Query("SELECT * FROM image")
    fun lecturaMultiple():List<ShowImage>
}
