package com.hussein.jwtauthktorandroid.auth

data class AuthState(
    val isLoading: Boolean = false,
    val signUpUserName : String = "",
    val signUpUserPassword : String = "",
    val signInUserName : String = "",
    val signInUserPassword : String = "",
    )
