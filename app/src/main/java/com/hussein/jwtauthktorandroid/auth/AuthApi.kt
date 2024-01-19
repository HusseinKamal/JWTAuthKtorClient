package com.hussein.jwtauthktorandroid.auth

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("signup")
    suspend fun singUp(
        @Body request : AuthRequest
    )

    @POST("signin")
    suspend fun singIn(
        @Body request : AuthRequest
    ):TokenResponse

    @GET("authenticate")
    suspend fun authentication(
        @Header("Authorization") token : String
    )
}