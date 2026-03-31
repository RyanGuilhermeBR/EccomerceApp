package com.example.eccomerceapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
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
import com.example.eccomerceapp.ui.theme.KabumOrange

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
            .height(340.dp), // Aumentado um pouco para caber tudo confortavelmente
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ProductImage(
                imageUrl = product.imageUrl,
                discountPercentage = product.discountPercentage
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

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

                // VALOR DO PRODUTO EM LARANJA
                Text(
                    text = "R$ ${String.format("%.2f", product.price)}",
                    style = MaterialTheme.typography.titleLarge,
                    color = KabumOrange, 
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "em até ${product.installments}x de R$ ${String.format("%.2f", product.getInstallmentValue())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.DarkGray,
                    fontSize = 11.sp
                )

                // ESTRELA EM LARANJA
                if (product.rating > 0) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = KabumOrange,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = product.rating.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
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

        if (discountPercentage != null && discountPercentage > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp),
                color = KabumOrange,
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
