package com.barrientos.pipocaspelis.BaseDeDatos

import android.content.Context
import androidx.room.*
import com.barrientos.pipocaspelis.Modelos.ShowImage
import com.barrientos.pipocaspelis.Modelos.ShowPrefered
import com.barrientos.pipocaspelis.Modelos.show

/*
CONECCION A LA BASE DE DATO DE SQLITE DE ANDROID
 */

@Database(entities = arrayOf(show::class, ShowImage::class, ShowPrefered::class), version = 5)
@TypeConverters(ListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ShowDAO(): ShowDAO
    abstract fun ImagenesDAO():ImagenesDAO
    abstract fun ShowPreferidoDAO(): ShowPreferidoDAO

/*
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            val tempIntance = INSTANCE
            if (tempIntance!= null) {
                return tempIntance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "Pelis&pipocas_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }*/

   companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Pelis&pipocas_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}