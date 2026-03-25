package com.example.eccomerceapp.data.repository

import com.example.eccomerceapp.data.api.AuthApi
import com.example.eccomerceapp.data.model.LoginRequest
import com.example.eccomerceapp.data.model.LoginResponse
import com.example.eccomerceapp.data.model.RegisterRequest
import com.example.eccomerceapp.data.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthRepository(private val authApi: AuthApi) {

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        Result.Success(body)
                    } else {
                        Result.Error(body.message)
                    }
                } else {
                    Result.Error("Erro ao fazer login: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Erro de conexão: ${e.message}", e)
            }
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                // Validações locais
                if (password != confirmPassword) {
                    return@withContext Result.Error("As senhas não coincidem")
                }
                if (password.length < 6) {
                    return@withContext Result.Error("A senha deve ter pelo menos 6 caracteres")
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    return@withContext Result.Error("Email inválido")
                }

                val response = authApi.register(
                    RegisterRequest(name, email, password, confirmPassword)
                )
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    if (body.success) {
                        Result.Success(body)
                    } else {
                        Result.Error(body.message)
                    }
                } else {
                    Result.Error("Erro ao fazer cadastro: ${response.message()}")
                }
            } catch (e: Exception) {
                Result.Error("Erro de conexão: ${e.message}", e)
            }
        }
    }

    fun validateEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePassword(password: String): Boolean {
        return password.length >= 6
    }
}
