# 🚀 Quick Start - EcommerceApp

## ⚡ Comandos Rápidos

### Executar Testes
```powershell
# Todos os testes
./gradlew test

# Testes específicos
./gradlew test --tests LoginViewModelTest
./gradlew test --tests RegisterViewModelTest
./gradlew test --tests AuthRepositoryTest
./gradlew test --tests HomeViewModelTest

# Com relatório detalhado
./gradlew test --info

# Gerar relatório HTML
./gradlew test
# Abrir: app/build/reports/tests/testDebugUnitTest/index.html
```

### Build e Run
```powershell
# Limpar build
./gradlew clean

# Build do projeto
./gradlew build

# Instalar no dispositivo
./gradlew installDebug

# Build + Testes
./gradlew clean build test
```

## 📱 Estrutura do Projeto

```
EccomerceApp/
├── app/src/main/java/com/example/eccomerceapp/
│   ├── data/                    # Camada de dados
│   │   ├── api/                 # APIs Retrofit
│   │   ├── model/               # Data classes
│   │   ├── repository/          # Repositórios
│   │   └── util/                # Utilitários
│   ├── presentation/            # Camada de apresentação
│   │   ├── login/               # Login (Screen + ViewModel)
│   │   ├── register/            # Cadastro (Screen + ViewModel)
│   │   ├── home/                # Home (Screen + ViewModel)
│   │   └── navigation/          # Navegação
│   └── ui/theme/                # Tema Material 3
│
├── app/src/test/java/           # TESTES UNITÁRIOS
│   ├── data/repository/         # Testes de Repositories
│   └── presentation/            # Testes de ViewModels
│
├── README.md                    # Documentação principal
├── TESTING_GUIDE.md             # Guia completo de testes
└── QUICK_START.md               # Este arquivo
```

## 🎯 Cenários de Teste Cobertos

### ✅ Login (11 testes)
- ✅ Login com credenciais válidas
- ✅ Email não cadastrado
- ✅ Senha incorreta
- ✅ Email inválido
- ✅ Senha vazia
- ✅ Erro de conexão
- ✅ Estados de loading
- ✅ Atualização de campos

### ✅ Cadastro (12 testes)
- ✅ Cadastro com dados válidos
- ✅ Nome vazio
- ✅ Email inválido
- ✅ Senha curta (< 6 caracteres)
- ✅ Senhas diferentes
- ✅ Email já cadastrado
- ✅ Estados de loading
- ✅ Atualização de campos

### ✅ Repository (13 testes)
- ✅ Login válido/inválido
- ✅ Cadastro com validações
- ✅ Erros de rede
- ✅ Validações de email/senha

### ✅ Home (9 testes)
- ✅ Carregamento de produtos
- ✅ Filtro por categoria
- ✅ Tratamento de erros
- ✅ Estados vazios

## 📊 Total: 45 Testes Implementados

## 🛠️ Tecnologias

- **Kotlin** - Linguagem
- **Jetpack Compose** - UI
- **Material 3** - Design
- **MVVM** - Arquitetura
- **Coroutines** - Assíncrono
- **Retrofit** - HTTP
- **StateFlow** - Estado
- **Navigation Compose** - Navegação

### Testes
- **JUnit 4** - Framework
- **MockK** - Mocking
- **Truth** - Assertions
- **Coroutines Test** - Async tests

## 🎨 Telas Implementadas

1. **Login Screen**
   - Email e senha
   - Validações em tempo real
   - Loading state
   - Mensagens de erro
   - Link para cadastro

2. **Register Screen**
   - Nome, email, senha e confirmação
   - Validações completas
   - Loading state
   - Mensagens de erro
   - Link para login

3. **Home Screen**
   - Grid de produtos
   - Loading state
   - Tratamento de erros
   - Botão de logout

## 🔧 Configuração Inicial

1. **Sincronizar Gradle**
   - Abra o projeto no Android Studio
   - Aguarde a sincronização automática
   - Ou clique em "Sync Now" se aparecer

2. **Configurar API (Opcional)**
   - Abra `NavGraph.kt`
   - Altere a `baseUrl` para sua API real
   - Atualmente: `https://api.example.com/`

3. **Executar App**
   - Conecte um dispositivo ou inicie um emulador
   - Clique em "Run" (▶️) ou pressione Shift+F10

## 📝 Próximos Passos Sugeridos

### Para Aprender Mais Sobre Testes:

1. **Adicione novos testes**
   - Teste edge cases
   - Teste cenários complexos
   - Aumente a cobertura

2. **Implemente TDD**
   - Escreva o teste primeiro
   - Faça o teste falhar
   - Implemente o código
   - Faça o teste passar
   - Refatore

3. **Adicione testes de UI**
   - Use Compose Testing
   - Teste interações do usuário
   - Teste navegação

### Para Expandir o App:

1. **Detalhes do Produto**
   - Tela de detalhes
   - ViewModel + Testes
   - Navegação

2. **Carrinho de Compras**
   - Adicionar/remover produtos
   - Calcular total
   - Persistência local

3. **Favoritos**
   - Marcar favoritos
   - Lista de favoritos
   - Room Database

4. **Busca**
   - Campo de busca
   - Filtros
   - Resultados

## 💡 Dicas

### Para Executar Testes Rapidamente:
- Use atalhos do Android Studio
- Ctrl+Shift+F10 (Windows) - Run teste atual
- Shift+F10 - Run último teste

### Para Debug de Testes:
- Coloque breakpoints nos testes
- Use Debug ao invés de Run
- Inspecione valores no debugger

### Para Ver Cobertura:
- Run → Run with Coverage
- Veja quais linhas foram testadas

## 📚 Recursos Úteis

- [Kotlin Coroutines Testing](https://kotlinlang.org/docs/coroutines-guide.html)
- [MockK Documentation](https://mockk.io/)
- [Truth Assertions](https://truth.dev/)
- [Jetpack Compose Testing](https://developer.android.com/jetpack/compose/testing)

## 🎓 O Que Você Aprendeu

✅ Arquitetura MVVM
✅ Jetpack Compose
✅ Material 3 Design
✅ Testes Unitários
✅ Mocking com MockK
✅ Coroutines Testing
✅ StateFlow
✅ Navigation Compose
✅ Retrofit
✅ Repository Pattern

## 🤝 Contribuindo

Este é um projeto de aprendizado. Sinta-se livre para:
- Adicionar mais testes
- Implementar novas features
- Melhorar o código existente
- Compartilhar conhecimento

---

**Bons estudos e bons testes! 🚀**
