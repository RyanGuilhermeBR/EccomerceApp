# 📝 Como Adicionar Novos Testes

## 🎯 Guia Prático para Criar Testes Unitários

Este guia mostra passo a passo como adicionar novos testes ao projeto.

## 📋 Template Básico de Teste

### 1. Estrutura Básica de um Teste de ViewModel

```kotlin
package com.example.eccomerceapp.presentation.novafeature

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.eccomerceapp.data.repository.SeuRepository
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
class SeuViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: SeuViewModel
    private lateinit var repository: SeuRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()
        viewModel = SeuViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `seu teste aqui`() = runTest {
        // Given (Arrange)
        // Preparar dados e mocks

        // When (Act)
        // Executar a ação

        // Then (Assert)
        // Verificar o resultado
    }
}
```

## 🔍 Exemplos Práticos

### Exemplo 1: Testar uma Função Simples

**Cenário:** Testar se um botão de favoritar funciona

```kotlin
@Test
fun `clicar em favoritar deve adicionar produto aos favoritos`() = runTest {
    // Given
    val produto = Product(id = "1", name = "Produto Teste", price = 100.0)
    coEvery { repository.addToFavorites(produto.id) } returns Result.Success(true)

    // When
    viewModel.toggleFavorite(produto.id)
    testDispatcher.scheduler.advanceUntilIdle()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.favoriteProducts).contains(produto.id)
    coVerify { repository.addToFavorites(produto.id) }
}
```

### Exemplo 2: Testar Validação

**Cenário:** Testar validação de CPF

```kotlin
@Test
fun `validar CPF inválido deve retornar erro`() = runTest {
    // Given
    val cpfInvalido = "123.456.789-00"

    // When
    viewModel.onCpfChange(cpfInvalido)
    viewModel.validateCpf()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.cpfError).isEqualTo("CPF inválido")
}

@Test
fun `validar CPF válido não deve retornar erro`() = runTest {
    // Given
    val cpfValido = "123.456.789-09" // CPF válido de exemplo

    // When
    viewModel.onCpfChange(cpfValido)
    viewModel.validateCpf()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.cpfError).isNull()
}
```

### Exemplo 3: Testar Loading State

**Cenário:** Verificar se o loading aparece durante uma requisição

```kotlin
@Test
fun `buscar produtos deve mostrar loading durante requisição`() = runTest {
    // Given
    val produtos = listOf(Product(id = "1", name = "Produto"))
    coEvery { repository.getProducts() } returns Result.Success(produtos)

    // When
    viewModel.loadProducts()

    // Then - Verificar loading ANTES de completar
    assertThat(viewModel.uiState.value.isLoading).isTrue()

    // Avançar o tempo para completar a coroutine
    testDispatcher.scheduler.advanceUntilIdle()

    // Then - Verificar loading DEPOIS de completar
    assertThat(viewModel.uiState.value.isLoading).isFalse()
    assertThat(viewModel.uiState.value.products).hasSize(1)
}
```

### Exemplo 4: Testar Erro de Rede

**Cenário:** Simular erro de conexão

```kotlin
@Test
fun `erro de rede deve mostrar mensagem apropriada`() = runTest {
    // Given
    val errorMessage = "Erro de conexão: Sem internet"
    coEvery { repository.getProducts() } returns Result.Error(errorMessage)

    // When
    viewModel.loadProducts()
    testDispatcher.scheduler.advanceUntilIdle()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.errorMessage).isEqualTo(errorMessage)
    assertThat(state.products).isEmpty()
}
```

### Exemplo 5: Testar Múltiplas Validações

**Cenário:** Formulário com vários campos

```kotlin
@Test
fun `formulário com todos os campos inválidos deve mostrar todos os erros`() = runTest {
    // Given
    val nomeVazio = ""
    val emailInvalido = "emailinvalido"
    val senhaFraca = "123"

    coEvery { repository.validateEmail(emailInvalido) } returns false
    coEvery { repository.validatePassword(senhaFraca) } returns false

    // When
    viewModel.onNameChange(nomeVazio)
    viewModel.onEmailChange(emailInvalido)
    viewModel.onPasswordChange(senhaFraca)
    viewModel.submit()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.nameError).isNotNull()
    assertThat(state.emailError).isNotNull()
    assertThat(state.passwordError).isNotNull()

    // Verificar que a API NÃO foi chamada
    coVerify(exactly = 0) { repository.submit(any()) }
}
```

### Exemplo 6: Testar Lista Vazia

**Cenário:** Quando não há produtos

```kotlin
@Test
fun `lista vazia deve mostrar estado apropriado`() = runTest {
    // Given
    coEvery { repository.getProducts() } returns Result.Success(emptyList())

    // When
    viewModel.loadProducts()
    testDispatcher.scheduler.advanceUntilIdle()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.products).isEmpty()
    assertThat(state.isLoading).isFalse()
    assertThat(state.errorMessage).isNull()
}
```

### Exemplo 7: Testar Filtro/Busca

**Cenário:** Buscar produtos por nome

```kotlin
@Test
fun `buscar produtos por nome deve filtrar corretamente`() = runTest {
    // Given
    val searchQuery = "notebook"
    val allProducts = listOf(
        Product(id = "1", name = "Notebook Dell"),
        Product(id = "2", name = "Mouse Logitech"),
        Product(id = "3", name = "Notebook HP")
    )
    val filteredProducts = allProducts.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    coEvery { repository.searchProducts(searchQuery) } returns Result.Success(filteredProducts)

    // When
    viewModel.onSearchQueryChange(searchQuery)
    viewModel.search()
    testDispatcher.scheduler.advanceUntilIdle()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.products).hasSize(2)
    assertThat(state.products.all { it.name.contains("Notebook") }).isTrue()
}
```

### Exemplo 8: Testar Atualização de Campo

**Cenário:** Verificar se campo é atualizado e erros são limpos

```kotlin
@Test
fun `atualizar email deve limpar erro anterior`() = runTest {
    // Given - Primeiro criar um erro
    viewModel.onEmailChange("emailinvalido")
    viewModel.validate()
    assertThat(viewModel.uiState.value.emailError).isNotNull()

    // When - Atualizar com novo valor
    val novoEmail = "novo@example.com"
    viewModel.onEmailChange(novoEmail)

    // Then
    val state = viewModel.uiState.value
    assertThat(state.email).isEqualTo(novoEmail)
    assertThat(state.emailError).isNull()
}
```

## 🧪 Testando Repository

### Template para Repository Test

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class SeuRepositoryTest {

    private lateinit var repository: SeuRepository
    private lateinit var api: SuaApi

    @Before
    fun setup() {
        api = mockk()
        repository = SeuRepository(api)
    }

    @Test
    fun `chamada de API com sucesso deve retornar Result Success`() = runTest {
        // Given
        val mockResponse = SuaResponse(data = "dados")
        coEvery { api.getData() } returns Response.success(mockResponse)

        // When
        val result = repository.getData()

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        val successResult = result as Result.Success
        assertThat(successResult.data).isEqualTo(mockResponse)
    }

    @Test
    fun `chamada de API com erro deve retornar Result Error`() = runTest {
        // Given
        coEvery { api.getData() } returns Response.error(
            404,
            "Not found".toResponseBody()
        )

        // When
        val result = repository.getData()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
    }

    @Test
    fun `exceção na API deve retornar Result Error`() = runTest {
        // Given
        coEvery { api.getData() } throws Exception("Network error")

        // When
        val result = repository.getData()

        // Then
        assertThat(result).isInstanceOf(Result.Error::class.java)
        val errorResult = result as Result.Error
        assertThat(errorResult.message).contains("Network error")
    }
}
```

## 📊 Checklist de Testes

Ao criar uma nova feature, teste:

### ✅ Cenários de Sucesso
- [ ] Operação bem-sucedida
- [ ] Dados corretos retornados
- [ ] Estado atualizado corretamente

### ✅ Cenários de Erro
- [ ] Erro de validação
- [ ] Erro de rede
- [ ] Erro de servidor
- [ ] Dados inválidos

### ✅ Estados
- [ ] Loading (início e fim)
- [ ] Success
- [ ] Error
- [ ] Empty (lista vazia)

### ✅ Validações
- [ ] Campos obrigatórios
- [ ] Formato de email
- [ ] Tamanho de senha
- [ ] Campos numéricos
- [ ] Datas

### ✅ Interações
- [ ] Atualização de campos
- [ ] Limpeza de erros
- [ ] Navegação
- [ ] Callbacks

## 🎯 Boas Práticas

### 1. Nome Descritivo
```kotlin
// ❌ Ruim
@Test
fun test1() { }

// ✅ Bom
@Test
fun `login com email inválido deve mostrar erro`() { }
```

### 2. Um Conceito por Teste
```kotlin
// ❌ Ruim - Testa múltiplas coisas
@Test
fun `teste completo`() {
    viewModel.login()
    assertThat(state.isLoading).isFalse()
    assertThat(state.user).isNotNull()
    assertThat(state.token).isNotEmpty()
}

// ✅ Bom - Testes separados
@Test
fun `login bem-sucedido deve parar loading`() { }

@Test
fun `login bem-sucedido deve retornar usuário`() { }

@Test
fun `login bem-sucedido deve retornar token`() { }
```

### 3. Arrange-Act-Assert
```kotlin
@Test
fun `exemplo`() = runTest {
    // Given (Arrange) - Preparar
    val dados = preparaDados()

    // When (Act) - Executar
    viewModel.acao()

    // Then (Assert) - Verificar
    assertThat(resultado).isTrue()
}
```

### 4. Usar Mocks Apropriadamente
```kotlin
// Mock de retorno
coEvery { repository.getData() } returns Result.Success(data)

// Verificar chamada
coVerify { repository.getData() }

// Verificar que NÃO foi chamado
coVerify(exactly = 0) { repository.getData() }

// Verificar número de chamadas
coVerify(exactly = 2) { repository.getData() }
```

## 🚀 Executando Seus Testes

```powershell
# Executar teste específico
./gradlew test --tests SeuViewModelTest

# Executar método específico
./gradlew test --tests SeuViewModelTest."seu teste aqui"

# Com output detalhado
./gradlew test --tests SeuViewModelTest --info
```

## 💡 Dicas Finais

1. **Escreva testes antes de implementar** (TDD)
2. **Mantenha testes simples e legíveis**
3. **Teste edge cases** (valores nulos, listas vazias, etc.)
4. **Use nomes descritivos** em português
5. **Isole cada teste** (não dependa de outros testes)
6. **Execute testes frequentemente**
7. **Mantenha alta cobertura** (>80%)

## 📚 Recursos Adicionais

- Veja os testes existentes em `app/src/test/java/`
- Consulte `TESTING_GUIDE.md` para mais detalhes
- Use `QUICK_START.md` para comandos rápidos

---

**Agora você está pronto para adicionar testes ao projeto! 🎉**
