package com.example.eccomerceapp.presentation.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eccomerceapp.data.model.LoginResponse
import com.example.eccomerceapp.data.model.User
import com.example.eccomerceapp.data.repository.AuthRepository
import com.example.eccomerceapp.data.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.every
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: LoginViewModel
    private lateinit var authRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Success(mockResponse)

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoginSuccessful).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
        coVerify { authRepository.login(email, password) }
    }

    @Test
    fun `login com email não cadastrado deve retornar erro`() = runTest {
        // Given
        val email = "naocadastrado@example.com"
        val password = "senha123"
        val errorMessage = "Email não cadastrado"

        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Error(errorMessage)

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoginSuccessful).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
        coVerify { authRepository.login(email, password) }
    }

    @Test
    fun `login com senha incorreta deve retornar erro`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senhaerrada"
        val errorMessage = "Senha incorreta"

        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Error(errorMessage)

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoginSuccessful).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
        coVerify { authRepository.login(email, password) }
    }

    @Test
    fun `login com email inválido deve mostrar erro de validação`() = runTest {
        // Given
        val invalidEmail = "emailinvalido"
        val password = "senha123"

        every { authRepository.validateEmail(invalidEmail) } returns false

        // When
        viewModel.onEmailChange(invalidEmail)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.emailError).isEqualTo("Email inválido")
        assertThat(state.isLoginSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `login com senha vazia deve mostrar erro de validação`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = ""

        every { authRepository.validateEmail(email) } returns true

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.passwordError).isEqualTo("Senha não pode estar vazia")
        assertThat(state.isLoginSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.login(any(), any()) }
    }

    @Test
    fun `login deve mostrar loading durante a requisição`() = runTest {
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

        every { authRepository.validateEmail(email) } returns true
        
        // ADICIONADO: delay para simular suspensão e manter o loading ativo
        coEvery { authRepository.login(email, password) } coAnswers {
            delay(1000) 
            Result.Success(mockResponse)
        }

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()

        // Executa até o ponto de suspensão (onde o delay acontece)
        runCurrent()

        // Then - Agora o isLoading deve ser true, pois a corrotina está "parada" no delay do mock
        assertThat(viewModel.uiState.value.isLoading).isTrue()

        // Avança o tempo para passar o delay e terminar a execução
        advanceUntilIdle()

        // Then - Após o término, o loading deve sumir
        assertThat(viewModel.uiState.value.isLoading).isFalse()
    }

    @Test
    fun `onEmailChange deve atualizar email e limpar erros`() = runTest {
        // Given
        val newEmail = "novo@example.com"

        // When
        viewModel.onEmailChange(newEmail)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.email).isEqualTo(newEmail)
        assertThat(state.emailError).isNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `onPasswordChange deve atualizar senha e limpar erros`() = runTest {
        // Given
        val newPassword = "novasenha123"

        // When
        viewModel.onPasswordChange(newPassword)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.password).isEqualTo(newPassword)
        assertThat(state.passwordError).isNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `clearError deve limpar mensagem de erro`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senha123"
        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Error("Erro de teste")

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `resetLoginSuccess deve resetar flag de sucesso`() = runTest {
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

        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Success(mockResponse)

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.resetLoginSuccess()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoginSuccessful).isFalse()
    }

    @Test
    fun `login com erro de conexão deve retornar erro apropriado`() = runTest {
        // Given
        val email = "teste@example.com"
        val password = "senha123"
        val errorMessage = "Erro de conexão: Sem internet"

        every { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.login(email, password) } returns Result.Error(errorMessage)

        // When
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.login()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isLoginSuccessful).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).contains("Erro de conexão")
    }
}
