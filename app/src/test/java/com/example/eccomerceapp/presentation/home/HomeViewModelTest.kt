package com.example.eccomerceapp.presentation.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eccomerceapp.data.model.Product
import com.example.eccomerceapp.data.repository.ProductRepository
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
class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private lateinit var productRepository: ProductRepository
    private val testDispatcher = StandardTestDispatcher()

    private val mockProducts = listOf(
        Product(
            id = "1",
            name = "Produto 1",
            description = "Descrição 1",
            price = 100.0,
            originalPrice = 150.0,
            discountPercentage = 33,
            installments = 10,
            imageUrl = "url1",
            category = "Eletrônicos",
            stock = 10,
            rating = 4.5f
        ),
        Product(
            id = "2",
            name = "Produto 2",
            description = "Descrição 2",
            price = 200.0,
            installments = 10,
            imageUrl = "url2",
            category = "Eletrônicos",
            stock = 5,
            rating = 4.0f
        )
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        productRepository = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init deve carregar produtos automaticamente`() = runTest {
        // Given
        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)

        // When
        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(2)
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
        coVerify { productRepository.getProducts() }
    }

    @Test
    fun `loadProducts com sucesso deve atualizar lista de produtos`() = runTest {
        // Given
        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)
        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.loadProducts()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(2)
        assertThat(state.products[0].name).isEqualTo("Produto 1")
        assertThat(state.products[1].name).isEqualTo("Produto 2")
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `loadProducts com erro deve mostrar mensagem de erro`() = runTest {
        // Given
        val errorMessage = "Erro ao buscar produtos"
        coEvery { productRepository.getProducts() } returns Result.Error(errorMessage)

        // When
        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.products).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isEqualTo(errorMessage)
    }

    @Test
    fun `loadProducts deve mostrar loading durante requisição`() = runTest {
        // Given
        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)

        // When
        viewModel = HomeViewModel(productRepository)

        // Then - Verificar estado de loading antes de avançar
        val loadingState = viewModel.uiState.value
        assertThat(loadingState.isLoading).isTrue()

        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Verificar estado após conclusão
        val finalState = viewModel.uiState.value
        assertThat(finalState.isLoading).isFalse()
    }

    @Test
    fun `loadProductsByCategory deve filtrar produtos por categoria`() = runTest {
        // Given
        val category = "Eletrônicos"
        val filteredProducts = mockProducts.filter { it.category == category }

        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)
        coEvery { productRepository.getProductsByCategory(category) } returns Result.Success(filteredProducts)

        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.loadProductsByCategory(category)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.products).hasSize(2)
        assertThat(state.selectedCategory).isEqualTo(category)
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
        coVerify { productRepository.getProductsByCategory(category) }
    }

    @Test
    fun `loadProductsByCategory com erro deve mostrar mensagem de erro`() = runTest {
        // Given
        val category = "Eletrônicos"
        val errorMessage = "Erro ao buscar produtos da categoria"

        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)
        coEvery { productRepository.getProductsByCategory(category) } returns Result.Error(errorMessage)

        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.loadProductsByCategory(category)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.errorMessage).isEqualTo(errorMessage)
        assertThat(state.selectedCategory).isEqualTo(category)
        assertThat(state.isLoading).isFalse()
    }

    @Test
    fun `clearCategoryFilter deve limpar filtro e recarregar todos produtos`() = runTest {
        // Given
        val category = "Eletrônicos"
        val filteredProducts = listOf(mockProducts[0])

        coEvery { productRepository.getProducts() } returns Result.Success(mockProducts)
        coEvery { productRepository.getProductsByCategory(category) } returns Result.Success(filteredProducts)

        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.loadProductsByCategory(category)
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.clearCategoryFilter()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.selectedCategory).isNull()
        assertThat(state.products).hasSize(2)
        assertThat(state.isLoading).isFalse()
        coVerify(exactly = 3) { productRepository.getProducts() } // init + clearFilter + reload
    }

    @Test
    fun `produtos vazios deve retornar lista vazia`() = runTest {
        // Given
        coEvery { productRepository.getProducts() } returns Result.Success(emptyList())

        // When
        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.products).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()
    }

    @Test
    fun `erro de conexão deve mostrar mensagem apropriada`() = runTest {
        // Given
        val errorMessage = "Erro de conexão: Sem internet"
        coEvery { productRepository.getProducts() } returns Result.Error(errorMessage)

        // When
        viewModel = HomeViewModel(productRepository)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertThat(state.errorMessage).contains("Erro de conexão")
        assertThat(state.products).isEmpty()
        assertThat(state.isLoading).isFalse()
    }
}
