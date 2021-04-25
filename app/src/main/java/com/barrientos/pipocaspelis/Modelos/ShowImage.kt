package com.barrientos.pipocaspelis.Modelos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "image")
data class ShowImage(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "idImage")
    val id: Long,
    @ColumnInfo(name = "mediano")
    val medium: String,
    @ColumnInfo(name = "original")
    val original:String
): Serializable