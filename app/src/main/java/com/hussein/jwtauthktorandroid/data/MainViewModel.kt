package com.hussein.jwtauthktorandroid.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hussein.jwtauthktorandroid.auth.AuthRepository
import com.hussein.jwtauthktorandroid.auth.AuthResult
import com.hussein.jwtauthktorandroid.auth.AuthState
import com.hussein.jwtauthktorandroid.auth.AuthUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
private val repository: AuthRepository
) :ViewModel(){

    var state by mutableStateOf(AuthState())
    //Used to send data to UI
    private val resultChannel = Channel<AuthResult<Unit>>()
    val authResult = resultChannel.receiveAsFlow()


    init {
        authenticate()
    }

    fun onEvent(event: AuthUiEvent){
        when(event){
            is AuthUiEvent.SignInUsernameChanged -> {
                state = state.copy(signInUserName = event.value)

            }
            is AuthUiEvent.SignInPasswordChanged -> {
                state = state.copy(signInUserPassword = event.value)

            }
            is AuthUiEvent.SignIn -> {
                signIn()
            }
            is AuthUiEvent.SignUpUsernameChanged -> {
                state = state.copy(signUpUserName = event.value)

            }
            is AuthUiEvent.SignUpPasswordChanged -> {
                state = state.copy(signUpUserPassword = event.value)
            }
            is AuthUiEvent.SignUp -> {
                signUp()
            }
        }
    }

    private fun signUp(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signUp(username = state.signUpUserName, password = state.signUpUserPassword)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun signIn(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.signIn(username = state.signInUserName, password = state.signInUserPassword)
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

    private fun authenticate(){
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            val result = repository.authenticate()
            resultChannel.send(result)
            state = state.copy(isLoading = false)
        }
    }

}