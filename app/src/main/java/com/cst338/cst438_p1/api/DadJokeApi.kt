package com.cst338.cst438_p1.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface DadJokeApi {
    @Headers("Accept: application/json")
    @GET("/")
    suspend fun getRandomJoke(): Response<DadJokeResponse>
}
