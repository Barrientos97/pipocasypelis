package com.barrientos.pipocaspelis.Modelos

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "show")

data class show (
        @PrimaryKey val id: Int,
    val url:String,
    val name:String,
    val officialSite: String?,
    val language: String,
    val genres:List<String>,
    val runtime:Int,
    val summary:String,
    val premiered:String,
        @Embedded val image:ShowImage

):Serializable
