# 🎨 Componentes Reutilizáveis - EcommerceApp

## 📦 ProductCard - Componente Principal

O **ProductCard** é um componente reutilizável que exibe informações de produtos de forma consistente em todo o aplicativo.

## 🎯 Características

### ✅ Funcionalidades Implementadas

1. **Preço com Desconto**
   - Exibe preço original riscado em cinza
   - Mostra preço atual em destaque
   - Badge de desconto no canto superior direito

2. **Parcelamento**
   - Calcula automaticamente o valor da parcela
   - Suporta diferentes números de parcelas (padrão: 10x)
   - Formato: "em até 10x de R$ XX,XX"

3. **Avaliação (Rating)**
   - Exibe estrela e nota quando disponível
   - Oculta quando rating = 0

4. **Badge de Desconto**
   - Aparece apenas quando há desconto
   - Formato: "-XX%"
   - Cor de destaque (vermelho)

5. **Responsivo**
   - Adapta-se a diferentes tamanhos de tela
   - Funciona em grid de 2 colunas
   - Altura fixa de 320dp

## 📊 Modelo de Dados Atualizado

```kotlin
data class Product(
    val id: String,
    val name: String,
    val description: String,
    val price: Double,                    // Preço atual (com desconto)
    val originalPrice: Double? = null,    // Preço original (sem desconto)
    val discountPercentage: Int? = null,  // Porcentagem de desconto
    val installments: Int = 10,           // Número de parcelas
    val imageUrl: String,
    val category: String,
    val stock: Int,
    val rating: Float = 0f
) {
    // Calcula o valor da parcela
    fun getInstallmentValue(): Double = price / installments

    // Verifica se tem desconto
    fun hasDiscount(): Boolean = originalPrice != null && originalPrice > price
}
```

## 🎨 Uso do Componente

### Exemplo Básico (Sem Desconto)

```kotlin
ProductCard(
    product = Product(
        id = "1",
        name = "Mouse Gamer RGB 16000 DPI",
        description = "Mouse gamer de alta precisão",
        price = 199.90,
        installments = 10,
        imageUrl = "",
        category = "Periféricos",
        stock = 10,
        rating = 4.5f
    ),
    onClick = { /* Ação ao clicar */ }
)
```

**Resultado:**
- Nome do produto
- Preço: R$ 199,90
- Parcelamento: em até 10x de R$ 19,99
- Rating: ⭐ 4.5

### Exemplo com Desconto

```kotlin
ProductCard(
    product = Product(
        id = "2",
        name = "Teclado Mecânico RGB",
        description = "Teclado mecânico profissional",
        price = 299.90,
        originalPrice = 499.90,
        discountPercentage = 40,
        installments = 10,
        imageUrl = "",
        category = "Periféricos",
        stock = 5,
        rating = 4.8f
    ),
    onClick = { /* Ação ao clicar */ }
)
```

**Resultado:**
- Badge: -40%
- Nome do produto
- Preço original riscado: ~~R$ 499,90~~
- Preço atual: R$ 299,90
- Parcelamento: em até 10x de R$ 29,99
- Rating: ⭐ 4.8

### Exemplo com Parcelamento Diferente

```kotlin
ProductCard(
    product = Product(
        id = "3",
        name = "Notebook Gamer RTX 4060",
        description = "Notebook potente",
        price = 5999.90,
        originalPrice = 7999.90,
        discountPercentage = 25,
        installments = 12,  // 12x ao invés de 10x
        imageUrl = "",
        category = "Notebooks",
        stock = 3,
        rating = 4.9f
    )
)
```

**Resultado:**
- Badge: -25%
- Preço original: ~~R$ 7.999,90~~
- Preço atual: R$ 5.999,90
- Parcelamento: em até 12x de R$ 499,99

## 🎭 Previews Disponíveis

O componente possui **6 previews** diferentes:

1. **ProductCardPreview** - Produto sem desconto
2. **ProductCardWithDiscountPreview** - Produto com desconto
3. **ProductCardDiscountNoRatingPreview** - Com desconto, sem rating
4. **ProductCardGridPreview** - Grid com 2 produtos
5. **ProductCardDarkPreview** - Dark mode
6. **ComponentsPreviewScreen** - Tela completa com todos os estados

### Como Visualizar os Previews

No Android Studio:
1. Abra o arquivo `ProductCard.kt`
2. Clique em "Split" ou "Design" no canto superior direito
3. Veja todos os previews renderizados

## 📱 Tela de Demonstração

Criamos uma tela dedicada para visualizar todos os componentes:

**Arquivo:** `ComponentsPreviewScreen.kt`

Esta tela mostra:
- ✅ ProductCard sem desconto
- ✅ ProductCard com desconto
- ✅ ProductCard sem avaliação
- ✅ ProductCard com desconto alto (50%)
- ✅ Grid de produtos (2 colunas)
- ✅ ProductCard com 12x parcelas
- ✅ ProductCard de produto premium

## 🎨 Elementos Visuais

### Cores e Estilos

```kotlin
// Preço Original (riscado)
- Cor: onSurfaceVariant (cinza)
- Decoração: LineThrough
- Tamanho: 12sp

// Preço Atual
- Cor: primary (azul/destaque)
- Peso: Bold
- Tamanho: 22sp

// Badge de Desconto
- Fundo: error (vermelho)
- Texto: onError (branco)
- Peso: Bold
- Tamanho: 12sp

// Parcelamento
- Cor: onSurfaceVariant (cinza)
- Tamanho: 11sp

// Rating
- Emoji: ⭐
- Tamanho: 12sp
```

### Dimensões

```kotlin
Card:
- Largura: fillMaxWidth
- Altura: 320dp

Imagem Placeholder:
- Largura: fillMaxWidth
- Altura: 160dp

Padding Interno:
- Geral: 12dp
- Badge: 8dp (horizontal), 4dp (vertical)

Espaçamentos:
- Entre elementos: 4dp - 8dp
```

## 🔧 Personalização

### Modificar Altura do Card

```kotlin
ProductCard(
    product = myProduct,
    modifier = Modifier.height(350.dp) // Altura customizada
)
```

### Adicionar Largura Específica

```kotlin
ProductCard(
    product = myProduct,
    modifier = Modifier
        .width(200.dp)
        .height(320.dp)
)
```

### Usar em Grid

```kotlin
LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
) {
    items(products) { product ->
        ProductCard(
            product = product,
            onClick = { navigateToDetails(product.id) }
        )
    }
}
```

## 📍 Onde é Usado

Atualmente o ProductCard é usado em:

1. **HomeScreen** - Grid de produtos na tela principal
2. **ComponentsPreviewScreen** - Tela de demonstração

### Futuras Implementações

Pode ser usado em:
- Tela de busca
- Tela de favoritos
- Tela de categoria
- Tela de ofertas
- Carrossel de produtos relacionados
- Lista de produtos no carrinho

## 🧪 Testes

### Cenários Testados

✅ Produto sem desconto
✅ Produto com desconto
✅ Produto sem rating
✅ Produto com desconto alto
✅ Diferentes números de parcelas
✅ Preços altos (formatação)
✅ Dark mode
✅ Grid layout

### Adicionar Testes Unitários (Futuro)

```kotlin
@Test
fun `product com desconto deve mostrar badge`() {
    val product = Product(
        id = "1",
        name = "Teste",
        price = 100.0,
        originalPrice = 200.0,
        discountPercentage = 50
    )

    assertThat(product.hasDiscount()).isTrue()
}

@Test
fun `calcular valor da parcela corretamente`() {
    val product = Product(
        id = "1",
        name = "Teste",
        price = 1000.0,
        installments = 10
    )

    assertThat(product.getInstallmentValue()).isEqualTo(100.0)
}
```

## 💡 Boas Práticas

### ✅ Fazer

- Sempre passar um `onClick` quando o card for clicável
- Usar `originalPrice` e `discountPercentage` juntos
- Manter `installments` entre 1 e 12
- Usar valores realistas para preços

### ❌ Evitar

- Não passar `discountPercentage` sem `originalPrice`
- Não usar valores negativos para preços
- Não usar `installments` = 0
- Não criar cards muito altos (> 400dp)

## 🚀 Próximas Melhorias

### Planejadas

- [ ] Adicionar suporte para imagens reais (Coil)
- [ ] Animação ao clicar
- [ ] Botão de favoritar
- [ ] Indicador de estoque baixo
- [ ] Selo de "Frete Grátis"
- [ ] Selo de "Mais Vendido"
- [ ] Skeleton loading
- [ ] Shimmer effect

### Possíveis Variações

- **ProductCardCompact** - Versão menor para listas
- **ProductCardHorizontal** - Layout horizontal
- **ProductCardDetailed** - Com mais informações
- **ProductCardCart** - Para carrinho de compras

## 📚 Referências

- [Material 3 Cards](https://m3.material.io/components/cards)
- [Jetpack Compose Layouts](https://developer.android.com/jetpack/compose/layouts)
- [Compose Preview](https://developer.android.com/jetpack/compose/tooling/previews)

## 🎓 Aprendizados

Com este componente você aprendeu:

✅ Como criar componentes reutilizáveis
✅ Como usar Previews no Compose
✅ Como trabalhar com estados condicionais (desconto, rating)
✅ Como formatar valores monetários
✅ Como criar layouts responsivos
✅ Como usar Material 3 components
✅ Como organizar código em pacotes

---

**Componente criado com foco em reutilização e boas práticas! 🎨✨**
