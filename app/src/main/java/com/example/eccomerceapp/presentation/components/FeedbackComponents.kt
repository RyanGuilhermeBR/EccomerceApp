package com.example.eccomerceapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eccomerceapp.ui.theme.EccomerceAppTheme

enum class FeedbackType {
    SUCCESS, ERROR, INFO
}

/**
 * Banner de feedback moderno para mensagens de erro ou sucesso
 */
@Composable
fun FeedbackBanner(
    message: String,
    type: FeedbackType,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true
) {
    val backgroundColor = when (type) {
        FeedbackType.SUCCESS -> Color(0xFFE8F5E9)
        FeedbackType.ERROR -> Color(0xFFFFEBEE)
        FeedbackType.INFO -> Color(0xFFE3F2FD)
    }

    val iconColor = when (type) {
        FeedbackType.SUCCESS -> Color(0xFF2E7D32)
        FeedbackType.ERROR -> Color(0xFFC62828)
        FeedbackType.INFO -> Color(0xFF1565C0)
    }

    val icon = when (type) {
        FeedbackType.SUCCESS -> Icons.Default.CheckCircle
        FeedbackType.ERROR -> Icons.Default.Warning
        FeedbackType.INFO -> Icons.Default.Info
    }

    AnimatedVisibility(
        visible = isVisible && message.isNotEmpty(),
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            color = backgroundColor,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 2.dp
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF1F2021),
                    fontWeight = FontWeight.Medium,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

/**
 * Componente para estados vazios ou erros de tela cheia
 */
@Composable
fun FullScreenMessage(
    title: String,
    description: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        
        if (buttonText != null && onButtonClick != null) {
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onButtonClick,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(0.7f)
            ) {
                Text(text = buttonText)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedbackPreview() {
    EccomerceAppTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            FeedbackBanner(
                message = "Produto adicionado ao carrinho com sucesso!",
                type = FeedbackType.SUCCESS
            )
            FeedbackBanner(
                message = "Ocorreu um erro ao processar seu pagamento. Tente novamente.",
                type = FeedbackType.ERROR
            )
            FeedbackBanner(
                message = "Este item possui estoque limitado.",
                type = FeedbackType.INFO
            )
        }
    }
}
