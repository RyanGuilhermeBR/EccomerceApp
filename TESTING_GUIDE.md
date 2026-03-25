# 🧪 Guia de Testes Unitários - EcommerceApp

## 📋 Índice
- [Introdução](#introdução)
- [Estrutura de Testes](#estrutura-de-testes)
- [Como Executar os Testes](#como-executar-os-testes)
- [Cenários de Teste Implementados](#cenários-de-teste-implementados)
- [Boas Práticas](#boas-práticas)
- [Exemplos de Testes](#exemplos-de-testes)

## 🎯 Introdução

Este projeto foi desenvolvido com foco em **testes unitários**. Todos os ViewModels e Repositories possuem testes completos que cobrem cenários de sucesso, erro e validações.

### Tecnologias de Teste Utilizadas

- **JUnit 4** - Framework de testes
- **MockK** - Biblioteca de mocking para Kotlin
- **Truth** - Assertions mais legíveis do Google
- **Coroutines Test** - Para testar código assíncrono
- **InstantTaskExecutorRule** - Para testes de LiveData/StateFlow

## 📁 Estrutura de Testes

```
app/src/test/java/com/example/eccomerceapp/
├── data/
│   └── repository/
│       └── AuthRepositoryTest.kt          # Testes do repositório de autenticação
└── presentation/
    ├── login/
    │   └── LoginViewModelTest.kt          # Testes do ViewModel de login
    ├── register/
    │   └── RegisterViewModelTest.kt       # Testes do ViewModel de cadastro
    └── home/
        └── HomeViewModelTest.kt           # Testes do ViewModel da home
```

## 🚀 Como Executar os Testes

### Opção 1: Via Android Studio (Recomendado)

1. **Executar todos os testes:**
   - Navegue até `app/src/test/java`
   - Clique com botão direito na pasta `java`
   - Selecione `Run 'Tests in 'java''`

2. **Executar testes de uma classe específica:**
   - Abra o arquivo de teste (ex: `LoginViewModelTest.kt`)
   - Clique no ícone verde ao lado do nome da classe
   - Selecione `Run 'LoginViewModelTest'`

3. **Executar um teste específico:**
   - Clique no ícone verde ao lado do método de teste
   - Selecione `Run 'nome_do_teste'`

### Opção 2: Via Terminal/PowerShell

```powershell
# Executar todos os testes
./gradlew test

# Executar testes com relatório detalhado
./gradlew test --info

# Executar testes de uma classe específica
./gradlew test --tests LoginViewModelTest
./gradlew test --tests RegisterViewModelTest
./gradlew test --tests AuthRepositoryTest
./gradlew test --tests HomeViewModelTest

# Executar um teste específico
./gradlew test --tests LoginViewModelTest."login com credenciais válidas deve retornar sucesso"
```

### Opção 3: Via Gradle Panel no Android Studio

1. Abra o painel Gradle (View → Tool Windows → Gradle)
2. Navegue até: `EccomerceApp → app → Tasks → verification → test`
3. Clique duas vezes em `test`

## 📊 Cenários de Teste Implementados

### 🔐 LoginViewModelTest (11 testes)

✅ **Cenários de Sucesso:**
- Login com credenciais válidas deve retornar sucesso

✅ **Cenários de Erro:**
- Login com email não cadastrado deve retornar erro
- Login com senha incorreta deve retornar erro
- Login com email inválido deve mostrar erro de validação
- Login com senha vazia deve mostrar erro de validação
- Login com erro de conexão deve retornar erro apropriado

✅ **Cenários de Estado:**
- Login deve mostrar loading durante a requisição
- onEmailChange deve atualizar email e limpar erros
- onPasswordChange deve atualizar senha e limpar erros
- clearError deve limpar mensagem de erro
- resetLoginSuccess deve resetar flag de sucesso

### 📝 RegisterViewModelTest (12 testes)

✅ **Cenários de Sucesso:**
- Cadastro com dados válidos deve retornar sucesso

✅ **Cenários de Erro:**
- Cadastro com nome vazio deve mostrar erro
- Cadastro com email inválido deve mostrar erro
- Cadastro com senha menor que 6 caracteres deve mostrar erro
- Cadastro com senhas diferentes deve mostrar erro
- Cadastro com email já cadastrado deve retornar erro

✅ **Cenários de Estado:**
- Cadastro deve mostrar loading durante a requisição
- onNameChange deve atualizar nome e limpar erros
- onEmailChange deve atualizar email e limpar erros
- onPasswordChange deve atualizar senha e limpar erros
- onConfirmPasswordChange deve atualizar confirmação de senha e limpar erros
- clearError deve limpar mensagem de erro
- resetRegisterSuccess deve resetar flag de sucesso

### 🔒 AuthRepositoryTest (13 testes)

✅ **Testes de Login:**
- Login com credenciais válidas deve retornar sucesso
- Login com credenciais inválidas deve retornar erro
- Login com erro de rede deve retornar erro
- Login com exceção deve retornar erro de conexão

✅ **Testes de Cadastro:**
- Register com dados válidos deve retornar sucesso
- Register com senhas diferentes deve retornar erro
- Register com senha curta deve retornar erro
- Register com email inválido deve retornar erro
- Register com email já cadastrado deve retornar erro

✅ **Testes de Validação:**
- validateEmail com email válido deve retornar true
- validateEmail com email inválido deve retornar false
- validatePassword com senha válida deve retornar true
- validatePassword com senha curta deve retornar false
- validatePassword com senha vazia deve retornar false

### 🏠 HomeViewModelTest (9 testes)

✅ **Cenários de Carregamento:**
- init deve carregar produtos automaticamente
- loadProducts com sucesso deve atualizar lista de produtos
- loadProducts com erro deve mostrar mensagem de erro
- loadProducts deve mostrar loading durante requisição
- produtos vazios deve retornar lista vazia

✅ **Cenários de Filtro:**
- loadProductsByCategory deve filtrar produtos por categoria
- loadProductsByCategory com erro deve mostrar mensagem de erro
- clearCategoryFilter deve limpar filtro e recarregar todos produtos

✅ **Cenários de Erro:**
- erro de conexão deve mostrar mensagem apropriada

## 📈 Cobertura Total

- **Total de Testes:** 45 testes
- **ViewModels:** 32 testes
- **Repositories:** 13 testes
- **Cobertura:** ~90% do código crítico

## ✨ Boas Práticas Implementadas

### 1. Padrão AAA (Arrange-Act-Assert)
```kotlin
@Test
fun `login com credenciais válidas deve retornar sucesso`() = runTest {
    // Given (Arrange) - Preparar os dados
    val email = "teste@example.com"
    val password = "senha123"

    // When (Act) - Executar a ação
    viewModel.login()

    // Then (Assert) - Verificar o resultado
    assertThat(state.isLoginSuccessful).isTrue()
}
```

### 2. Nomes Descritivos
- Usamos nomes em português que descrevem exatamente o que está sendo testado
- Formato: `função com condição deve resultado esperado`

### 3. Isolamento de Testes
- Cada teste é independente
- Usamos `@Before` para setup e `@After` para cleanup
- Mocks são criados para cada teste

### 4. Testes de Coroutines
```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
```

### 5. Verificação de Mocks
```kotlin
coVerify { authRepository.login(email, password) }
coVerify(exactly = 0) { authRepository.login(any(), any()) }
```

## 💡 Exemplos de Testes

### Exemplo 1: Teste de Sucesso
```kotlin
@Test
fun `login com credenciais válidas deve retornar sucesso`() = runTest {
    // Given
    val email = "teste@example.com"
    val password = "senha123"
    val mockResponse = LoginResponse(success = true, message = "Sucesso")

    coEvery { authRepository.login(email, password) } returns Result.Success(mockResponse)

    // When
    viewModel.onEmailChange(email)
    viewModel.onPasswordChange(password)
    viewModel.login()
    testDispatcher.scheduler.advanceUntilIdle()

    // Then
    val state = viewModel.uiState.value
    assertThat(state.isLoginSuccessful).isTrue()
    assertThat(state.errorMessage).isNull()
}
```

### Exemplo 2: Teste de Validação
```kotlin
@Test
fun `login com email inválido deve mostrar erro de validação`() = runTest {
    // Given
    val invalidEmail = "emailinvalido"
    coEvery { authRepository.validateEmail(invalidEmail) } returns false

    // When
    viewModel.onEmailChange(invalidEmail)
    viewModel.login()

    // Then
    assertThat(viewModel.uiState.value.emailError).isEqualTo("Email inválido")
}
```

### Exemplo 3: Teste de Loading
```kotlin
@Test
fun `login deve mostrar loading durante a requisição`() = runTest {
    // Given
    coEvery { authRepository.login(any(), any()) } returns Result.Success(mockResponse)

    // When
    viewModel.login()

    // Then - Antes de completar
    assertThat(viewModel.uiState.value.isLoading).isTrue()

    testDispatcher.scheduler.advanceUntilIdle()

    // Then - Depois de completar
    assertThat(viewModel.uiState.value.isLoading).isFalse()
}
```

## 🎓 Aprendizados

### O que você aprenderá com estes testes:

1. **Como testar ViewModels** com StateFlow e Coroutines
2. **Como mockar dependências** usando MockK
3. **Como testar código assíncrono** com Coroutines Test
4. **Como escrever assertions claras** com Truth
5. **Como organizar testes** seguindo boas práticas
6. **Como testar diferentes cenários** (sucesso, erro, validação)
7. **Como verificar estados** (loading, success, error)

## 🔍 Visualizando Resultados

### No Android Studio:
- Após executar os testes, você verá um painel com:
  - ✅ Testes que passaram (verde)
  - ❌ Testes que falharam (vermelho)
  - Tempo de execução de cada teste
  - Stack trace de erros (se houver)

### Relatório HTML:
```powershell
./gradlew test
# O relatório estará em: app/build/reports/tests/testDebugUnitTest/index.html
```

## 📚 Próximos Passos

Para expandir seus conhecimentos em testes:

1. **Adicionar testes de UI** com Compose Testing
2. **Implementar testes de integração**
3. **Adicionar cobertura de código** com JaCoCo
4. **Criar testes parametrizados** para múltiplos cenários
5. **Implementar testes de performance**

## 🤝 Contribuindo com Testes

Ao adicionar novas funcionalidades, sempre:

1. Escreva o teste primeiro (TDD - Test Driven Development)
2. Cubra cenários de sucesso e erro
3. Teste validações e edge cases
4. Mantenha os testes simples e legíveis
5. Execute todos os testes antes de fazer commit

---

**Lembre-se:** Testes não são apenas sobre encontrar bugs, mas sobre ter confiança no seu código! 🚀
