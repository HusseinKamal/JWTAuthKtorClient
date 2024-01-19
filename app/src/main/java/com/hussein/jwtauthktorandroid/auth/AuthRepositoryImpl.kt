package com.hussein.jwtauthktorandroid.auth

import android.content.SharedPreferences


class AuthRepositoryImpl(
    private val api: AuthApi,
    private val prefs:SharedPreferences
) : AuthRepository {
    override suspend fun signUp(username: String, password: String): AuthResult<Unit> {
        return try {
            api.singUp(request = AuthRequest(
                username, password
            ))
            signIn(username, password)
        }catch (e:retrofit2.HttpException){
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        }catch (e:Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun signIn(username: String, password: String): AuthResult<Unit> {
        return try {
            val response =api.singIn(
                request = AuthRequest(
                username, password
            ))
            prefs.edit().putString("jwt",response.token).apply()
            AuthResult.Authorized()
        }catch (e:retrofit2.HttpException){
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        }catch (e:Exception){
            AuthResult.UnknownError()
        }
    }

    override suspend fun authenticate(): AuthResult<Unit> {
        return try {
            val token = prefs.getString("jwt",null) ?: return AuthResult.Unauthorized()
            api.authentication("Bearer $token")
            AuthResult.Authorized()
        }catch (e:retrofit2.HttpException){
            if(e.code() == 401){
                AuthResult.Unauthorized()
            }
            else{
                AuthResult.UnknownError()
            }
        }catch (e:Exception){
            AuthResult.UnknownError()
        }
    }
}