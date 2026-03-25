package com.example.eccomerceapp.presentation.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eccomerceapp.data.model.LoginResponse
import com.example.eccomerceapp.data.model.User
import com.example.eccomerceapp.data.repository.AuthRepository
import com.example.eccomerceapp.data.util.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.assertThat

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        authRepository = mockk()
        viewModel = RegisterViewModel(authRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `cadastro com dados válidos deve retornar sucesso`() = runTest {
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

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true
        coEvery { authRepository.register(name, email, password, confirmPassword) } returns Result.Success(mockResponse)

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isRegisterSuccessful).isTrue()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
        coVerify { authRepository.register(name, email, password, confirmPassword) }
    }

    @Test
    fun `cadastro com nome vazio deve mostrar erro`() = runTest {
        // Given
        val name = ""
        val email = "teste@example.com"
        val password = "senha123"
        val confirmPassword = "senha123"

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.nameError).isEqualTo("Nome não pode estar vazio")
        assertThat(state.isRegisterSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.register(any(), any(), any(), any()) }
    }

    @Test
    fun `cadastro com email inválido deve mostrar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "emailinvalido"
        val password = "senha123"
        val confirmPassword = "senha123"

        coEvery { authRepository.validateEmail(email) } returns false

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.emailError).isEqualTo("Email inválido")
        assertThat(state.isRegisterSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.register(any(), any(), any(), any()) }
    }

    @Test
    fun `cadastro com senha menor que 6 caracteres deve mostrar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "123"
        val confirmPassword = "123"

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns false

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.passwordError).isEqualTo("A senha deve ter pelo menos 6 caracteres")
        assertThat(state.isRegisterSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.register(any(), any(), any(), any()) }
    }

    @Test
    fun `cadastro com senhas diferentes deve mostrar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "senha123"
        val confirmPassword = "senha456"

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.confirmPasswordError).isEqualTo("As senhas não coincidem")
        assertThat(state.isRegisterSuccessful).isFalse()
        coVerify(exactly = 0) { authRepository.register(any(), any(), any(), any()) }
    }

    @Test
    fun `cadastro com email já cadastrado deve retornar erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "existente@example.com"
        val password = "senha123"
        val confirmPassword = "senha123"
        val errorMessage = "Email já cadastrado"

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true
        coEvery { authRepository.register(name, email, password, confirmPassword) } returns Result.Error(errorMessage)

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isRegisterSuccessful).isFalse()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
        coVerify { authRepository.register(name, email, password, confirmPassword) }
    }

    @Test
    fun `cadastro deve mostrar loading durante a requisição`() = runTest {
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

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true
        coEvery { authRepository.register(name, email, password, confirmPassword) } returns Result.Success(mockResponse)

        // When
        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()

        // Then - Verificar estado de loading antes de avançar
        val loadingState = viewModel.uiState.value
        assertThat(loadingState.isLoading).isTrue()

        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Verificar estado após conclusão
        val finalState = viewModel.uiState.value
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `onNameChange deve atualizar nome e limpar erros`() = runTest {
        // Given
        val newName = "Novo Nome"

        // When
        viewModel.onNameChange(newName)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.name).isEqualTo(newName)
        assertThat(state.nameError).isNull()
        assertThat(state.errorMessage).isNull()
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
    fun `onConfirmPasswordChange deve atualizar confirmação de senha e limpar erros`() = runTest {
        // Given
        val newConfirmPassword = "novasenha123"

        // When
        viewModel.onConfirmPasswordChange(newConfirmPassword)

        // Then
        val state = viewModel.uiState.value
        assertThat(state.confirmPassword).isEqualTo(newConfirmPassword)
        assertThat(state.confirmPasswordError).isNull()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `clearError deve limpar mensagem de erro`() = runTest {
        // Given
        val name = "Teste Usuario"
        val email = "teste@example.com"
        val password = "senha123"
        val confirmPassword = "senha123"

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true
        coEvery { authRepository.register(name, email, password, confirmPassword) } returns Result.Error("Erro de teste")

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `resetRegisterSuccess deve resetar flag de sucesso`() = runTest {
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

        coEvery { authRepository.validateEmail(email) } returns true
        coEvery { authRepository.validatePassword(password) } returns true
        coEvery { authRepository.register(name, email, password, confirmPassword) } returns Result.Success(mockResponse)

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(confirmPassword)
        viewModel.register()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.resetRegisterSuccess()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.isRegisterSuccessful).isFalse()
    }
}
