package com.example.eccomerceapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eccomerceapp.data.model.Product
import com.example.eccomerceapp.ui.theme.EccomerceAppTheme

/**
 * Tela de Preview de Componentes Reutilizáveis
 *
 * Esta tela serve para visualizar todos os componentes reutilizáveis
 * do aplicativo em diferentes estados e configurações.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentsPreviewScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Componentes Reutilizáveis") }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Seção: ProductCard Básico
            item {
                SectionTitle("ProductCard - Sem Desconto")
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
                    onClick = { }
                )
            }

            // Seção: ProductCard com Desconto
            item {
                SectionTitle("ProductCard - Com Desconto")
                ProductCard(
                    product = Product(
                        id = "2",
                        name = "Teclado Mecânico RGB Switch Blue",
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
                    onClick = { }
                )
            }

            // Seção: ProductCard sem Rating
            item {
                SectionTitle("ProductCard - Sem Avaliação")
                ProductCard(
                    product = Product(
                        id = "3",
                        name = "Headset Gamer 7.1 Surround",
                        description = "Headset com som surround",
                        price = 149.90,
                        originalPrice = 249.90,
                        discountPercentage = 40,
                        installments = 10,
                        imageUrl = "",
                        category = "Áudio",
                        stock = 15,
                        rating = 0f
                    ),
                    onClick = { }
                )
            }

            // Seção: ProductCard com Desconto Alto
            item {
                SectionTitle("ProductCard - Desconto Alto (50%)")
                ProductCard(
                    product = Product(
                        id = "4",
                        name = "SSD NVMe 1TB Gen4",
                        description = "SSD de alta velocidade",
                        price = 449.90,
                        originalPrice = 899.90,
                        discountPercentage = 50,
                        installments = 10,
                        imageUrl = "",
                        category = "Armazenamento",
                        stock = 20,
                        rating = 5.0f
                    ),
                    onClick = { }
                )
            }

            // Seção: Grid de ProductCards
            item {
                SectionTitle("Grid de Produtos (2 colunas)")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.height(700.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(getSampleProducts()) { product ->
                        ProductCard(
                            product = product,
                            onClick = { }
                        )
                    }
                }
            }

            // Seção: ProductCard com Parcelamento Diferente
            item {
                SectionTitle("ProductCard - 12x sem juros")
                ProductCard(
                    product = Product(
                        id = "5",
                        name = "Notebook Gamer RTX 4060",
                        description = "Notebook potente para jogos",
                        price = 5999.90,
                        originalPrice = 7999.90,
                        discountPercentage = 25,
                        installments = 12,
                        imageUrl = "",
                        category = "Notebooks",
                        stock = 3,
                        rating = 4.9f
                    ),
                    onClick = { }
                )
            }

            // Seção: ProductCard Preço Alto
            item {
                SectionTitle("ProductCard - Produto Premium")
                ProductCard(
                    product = Product(
                        id = "6",
                        name = "Placa de Vídeo RTX 4090",
                        description = "Top de linha para profissionais",
                        price = 12999.90,
                        originalPrice = 15999.90,
                        discountPercentage = 18,
                        installments = 10,
                        imageUrl = "",
                        category = "Hardware",
                        stock = 1,
                        rating = 5.0f
                    ),
                    onClick = { }
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

// Função auxiliar para gerar produtos de exemplo
private fun getSampleProducts(): List<Product> {
    return listOf(
        Product(
            id = "1",
            name = "Monitor 27\" 144Hz",
            description = "Monitor gamer",
            price = 1299.90,
            originalPrice = 1799.90,
            discountPercentage = 27,
            installments = 10,
            imageUrl = "",
            category = "Monitores",
            stock = 8,
            rating = 4.6f
        ),
        Product(
            id = "2",
            name = "Cadeira Gamer RGB",
            description = "Cadeira ergonômica",
            price = 899.90,
            installments = 10,
            imageUrl = "",
            category = "Móveis",
            stock = 12,
            rating = 4.3f
        ),
        Product(
            id = "3",
            name = "Webcam Full HD 1080p",
            description = "Webcam para streaming",
            price = 299.90,
            originalPrice = 499.90,
            discountPercentage = 40,
            installments = 10,
            imageUrl = "",
            category = "Periféricos",
            stock = 25,
            rating = 4.7f
        ),
        Product(
            id = "4",
            name = "Microfone Condensador",
            description = "Microfone profissional",
            price = 549.90,
            originalPrice = 799.90,
            discountPercentage = 31,
            installments = 10,
            imageUrl = "",
            category = "Áudio",
            stock = 6,
            rating = 4.9f
        )
    )
}

// Preview da tela completa
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ComponentsPreviewScreenPreview() {
    EccomerceAppTheme {
        ComponentsPreviewScreen()
    }
}

// Preview Dark Mode
@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ComponentsPreviewScreenDarkPreview() {
    EccomerceAppTheme {
        ComponentsPreviewScreen()
    }
}
