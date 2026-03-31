package com.example.eccomerceapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eccomerceapp.data.model.Product
import com.example.eccomerceapp.presentation.components.ProductCard
import com.example.eccomerceapp.ui.theme.EccomerceAppTheme
import com.example.eccomerceapp.ui.theme.KabumBlue

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeContent(
        uiState = uiState,
        onLogout = onLogout,
        onRefresh = viewModel::loadProducts
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    uiState: HomeUiState,
    onLogout: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Surface(
                        onClick = { /* TODO: Abrir busca */ },
                        shape = RoundedCornerShape(4.dp),
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .padding(horizontal = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Busque aqui...",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(start = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Placeholder Logo
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Ver perfil */ }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Meu Perfil",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = KabumBlue, // FUNDO TOP BAR EM AZUL
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            item {
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                    uiState.errorMessage != null -> {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
                            Button(onClick = onRefresh) { Text("Tentar novamente") }
                        }
                    }
                    else -> {
                        // SEÇÃO DESTAQUES NINJAS
                        Column(modifier = Modifier.padding(vertical = 16.dp)) {
                            Text(
                                text = "DESTAQUES NINJAS",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = KabumBlue
                            )
                            
                            LazyRow(
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.products) { product ->
                                    ProductCard(
                                        product = product,
                                        onClick = { },
                                        modifier = Modifier.width(160.dp) // Ajustado para aparecerem 3 em telas comuns
                                    )
                                }
                            }
                        }

                        // GRID DE TODOS OS PRODUTOS (Exemplo de continuação da tela)
                        Text(
                            text = "TODOS OS PRODUTOS",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Se quisermos manter o grid abaixo da seção ninja
            if (uiState.products.isNotEmpty() && uiState.errorMessage == null && !uiState.isLoading) {
                items(uiState.products.chunked(2)) { rowProducts ->
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductCard(
                                product = product,
                                onClick = { },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (rowProducts.size == 1) Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val mockProducts = listOf(
        Product(id = "1", name = "Smartphone XYZ", description = "", price = 1999.99, originalPrice = 2499.0, discountPercentage = 20, imageUrl = "", category = "", stock = 10, rating = 4.5f),
        Product(id = "2", name = "Notebook ABC", description = "", price = 4500.0, imageUrl = "", category = "", stock = 5, rating = 4.8f),
        Product(id = "3", name = "Mouse Gamer", description = "", price = 150.0, imageUrl = "", category = "", stock = 20, rating = 4.2f),
        Product(id = "4", name = "Teclado Mecânico", description = "", price = 350.0, imageUrl = "", category = "", stock = 15, rating = 4.7f)
    )
    
    EccomerceAppTheme {
        HomeContent(
            uiState = HomeUiState(products = mockProducts),
            onLogout = {},
            onRefresh = {}
        )
    }
}
