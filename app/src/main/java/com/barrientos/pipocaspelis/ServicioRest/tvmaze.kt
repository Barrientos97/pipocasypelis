package com.barrientos.pipocaspelis.ServicioRest

import com.barrientos.pipocaspelis.Modelos.ContenedorShow
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface tvmaze {

    @GET("search/shows?")
    fun listarPelicula(@Query("q")filtro:String): Call<List<ContenedorShow>>
}