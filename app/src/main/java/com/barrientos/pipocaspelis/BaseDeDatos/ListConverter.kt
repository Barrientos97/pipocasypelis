package com.barrientos.pipocaspelis.BaseDeDatos

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListConverter {

    //aqui devuelve una lista de String
    @TypeConverter
    fun fromString(value :String):List<String>{
        val lisType : Type = object  : TypeToken<List<String>>(){}.type
        return Gson().fromJson(value,lisType)
    }


    //aqui guardara una lista de String  en la bd
    @TypeConverter
    fun fromList(list: List<String>):String{

        val gson= Gson()
        return gson.toJson(list)
    }
}