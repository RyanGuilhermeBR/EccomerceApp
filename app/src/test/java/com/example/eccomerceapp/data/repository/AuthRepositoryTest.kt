package com.example.eccomerceapp.data.repository

import com.example.eccomerceapp.data.api.AuthApi
import com.example.eccomerceapp.data.model.LoginRequest
import com.example.eccomerceapp.data.model.LoginResponse
import com.example.eccomerceapp.data.model.RegisterRequest
import com.example.eccomerceapp.data.model.User
import com.example.eccomerceapp.data.util.Result
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var authApi: AuthApi

    @Before
    fun setup() {
        authApi = mockk()
        authRepository = AuthRepository(authApi)
    }

    @Test
    fun `login com credenciais válidas deve retornar sucesso`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senha123"
        val mockUser = User(id = "1", email = email, name = "Teste", token = "token123")
        val mockResponse = LoginResponse(
            success = true,
            message = "Login realizado com sucesso",
            user = mockUser,
            token = "token123"
        )

        coEvery { authApi.login(LoginRequest(email, password)) } returns Response.success(mockResponse)

        // When
        val result = authRepository.login(email, password)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data.success).isTrue()
        assertThat(successResult.data.user?.email).isEqualTo(email)
    }

    @Test
    fun `login com credenciais inválidas deve retornar erro`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senhaerrada"
        val mockResponse = LoginResponse(
            success = false,
            message = "Credenciais inválidas",
            user = null,
            token = null
        )

        coEvery { authApi.login(LoginRequest(email, password)) } returns Response.success(mockResponse)

        // When
        val result = authRepository.login(email, password)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo("Credenciais inválidas")
    }

    @Test
    fun `login com erro de rede deve retornar erro`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senha123"

        coEvery { authApi.login(LoginRequest(email, password)) } returns Response.error(
            500,
            "Server error".toResponseBody()
        )

        // When
        val result = authRepository.login(email, password)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Erro ao fazer login")
    }

    @Test
    fun `login com exceção deve retornar erro de conexão`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senha123"

        coEvery { authApi.login(LoginRequest(email, password)) } throws Exception("Network error")

        // When
        val result = authRepository.login(email, password)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Erro de conexão")
    }

    @Test
    fun `register com dados válidos deve retornar sucesso`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "senha123"
        val confirmPassword = "senha123"
        val mockUser = User(id = "1", email = email, name = name, token = "token123")
        val mockResponse = LoginResponse(
            success = true,
            message = "Cadastro realizado com sucesso",
            user = mockUser,
            token = "token123"
        )

        coEvery {
            authApi.register(RegisterRequest(name, email, password, confirmPassword))
        } returns Response.success(mockResponse)

        // When
        val result = authRepository.register(name, email, password, confirmPassword)

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data.success).isTrue()
        assertThat(successResult.data.user?.name).isEqualTo(name)
    }

    @Test
    fun `register com senhas diferentes deve retornar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "senha123"
        val confirmPassword = "senha456"

        // When
        val result = authRepository.register(name, email, password, confirmPassword)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo("As senhas não coincidem")
    }

    @Test
    fun `register com senha curta deve retornar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "123"
        val confirmPassword = "123"

        // When
        val result = authRepository.register(name, email, password, confirmPassword)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo("A senha deve ter pelo menos 6 caracteres")
    }

    @Test
    fun `register com email inválido deve retornar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "emailinvalido"
        val password = "senha123"
        val confirmPassword = "senha123"

        // When
        val result = authRepository.register(name, email, password, confirmPassword)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo("Email inválido")
    }

    @Test
    fun `register com email já cadastrado deve retornar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "existente@example.com"
        val password = "senha123"
        val confirmPassword = "senha123"
        val mockResponse = LoginResponse(
            success = false,
            message = "Email já cadastrado",
            user = null,
            token = null
        )

        coEvery {
            authApi.register(RegisterRequest(name, email, password, confirmPassword))
        } returns Response.success(mockResponse)

        // When
        val result = authRepository.register(name, email, password, confirmPassword)

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).isEqualTo("Email já cadastrado")
    }

    @Test
    fun `validateEmail com email válido deve retornar true`() {
        // Given
        val validEmail = "teste@example.com"

        // When
        val result = authRepository.validateEmail(validEmail)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `validateEmail com email inválido deve retornar false`() {
        // Given
        val invalidEmail = "emailinvalido"

        // When
        val result = authRepository.validateEmail(invalidEmail)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `validatePassword com senha válida deve retornar true`() {
        // Given
        val validPassword = "senha123"

        // When
        val result = authRepository.validatePassword(validPassword)

        // Then
        assertThat(result).isTrue()
    }

    @Test
    fun `validatePassword com senha curta deve retornar false`() {
        // Given
        val shortPassword = "123"

        // When
        val result = authRepository.validatePassword(shortPassword)

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `validatePassword com senha vazia deve retornar false`() {
        // Given
        val emptyPassword = ""

        // When
        val result = authRepository.validatePassword(emptyPassword)

        // Then
        assertThat(result).isFalse()
    }
}
