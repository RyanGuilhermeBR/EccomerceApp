package com.example.eccomerceapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eccomerceapp.data.model.Product
import com.example.eccomerceapp.ui.theme.EccomerceAppTheme

/**
 * Card de produto reutilizável inspirado no design da Kabum
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(320.dp),
        onClick = onClick,
        // Forçando o fundo do card para Branco
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Imagem do Produto
            ProductImage(
                imageUrl = product.imageUrl,
                discountPercentage = product.discountPercentage
            )

            // Informações do Produto
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Nome do Produto
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Preço Original (se houver desconto)
                if (product.hasDiscount()) {
                    Text(
                        text = "R$ ${String.format("%.2f", product.originalPrice)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                }

                // Preço Atual em LARANJA
                Text(
                    text = "R$ ${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.secondary, // Mapeado para KabumOrange
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Parcelamento
                Text(
                    text = "em até ${product.installments}x de R$ ${String.format("%.2f", product.getInstallmentValue())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    fontSize = 11.sp
                )

                // Rating
                if (product.rating > 0) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⭐", fontSize = 10.sp)
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = product.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProductImage(
    imageUrl: String,
    discountPercentage: Int?
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        // Fundo da imagem levemente cinza para destacar o produto
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFFF8F9FA) 
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                )
            }
        }

        // Badge de Desconto Laranja
        if (discountPercentage != null && discountPercentage > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = MaterialTheme.colorScheme.secondary,
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    text = "-$discountPercentage%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
            }
        }
    }
}

// Previews atualizadas para mostrar o novo estilo
@Preview(name = "Produto Kabum Style", showBackground = true, backgroundColor = 0xFFF2F3F4)
@Composable
private fun ProductCardKabumPreview() {
    EccomerceAppTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            ProductCard(
                product = Product(
                    id = "1",
                    name = "Placa de Vídeo RTX 4060 Ti MSI NVIDIA GeForce, 8GB GDDR6",
                    description = "Alta performance gamer",
                    price = 2399.90,
                    originalPrice = 2999.00,
                    discountPercentage = 20,
                    installments = 10,
                    imageUrl = "",
                    category = "Hardware",
                    stock = 10,
                    rating = 4.9f
                ),
                onClick = { }
            )
        }
    }
}
