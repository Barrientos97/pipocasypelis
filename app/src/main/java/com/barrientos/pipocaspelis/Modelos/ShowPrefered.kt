package com.barrientos.pipocaspelis.Modelos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preferidos")
data class ShowPrefered(
    @PrimaryKey(autoGenerate = true)
    val id : Long,
    val showId : Long,
    var esFAVORITO: Boolean

)