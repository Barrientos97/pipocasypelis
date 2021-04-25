package com.barrientos.pipocaspelis.ServicioRest

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class servicios {
    val BASE_URL = "http://api.tvmaze.com/"
    lateinit var retrofit: Retrofit


    constructor(){
    retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }

    fun getTVMaze(): tvmaze{
        return retrofit.create(tvmaze::class.java)
    }
}