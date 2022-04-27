package com.ahmetkanat.cat.service


import com.ahmetkanat.cat.model.Cat
import io.reactivex.Single
import retrofit2.http.GET

interface CatAPI {
    @GET("breeds?api_key=a75e92c1-0b57-4127-9e1e-db328dafb529")
    fun getCat() : Single<List<Cat>>

}