package com.example.ejercicio1.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.ui.theme.Amber
import com.example.ejercicio1.ui.theme.Background
import com.example.ejercicio1.ui.theme.Coral
import com.example.ejercicio1.ui.theme.TextMuted
import java.util.Locale

fun Double.formatPrice(): String = String.format(Locale.US, "\$%.2f", this)

@Composable
fun RatingBar(rating: Double, starSize: Dp = 14.dp) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        repeat(5) { index ->
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = if (index < Math.round(rating)) Amber else Color(0xFFE5E7EB),
                modifier = Modifier.size(starSize)
            )
        }
    }
}

@Composable
fun DiscountBadge(discount: Double, modifier: Modifier = Modifier) {
    Text(
        text = "-${Math.round(discount)}%",
        color = Color.White,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(Coral)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

@Composable
fun ProductCard(product: Product, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Background)
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            )
            if (product.discountPercentage >= 1) {
                DiscountBadge(
                    discount = product.discountPercentage,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                )
            }
        }
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = product.category.uppercase(),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.height(40.dp)
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingBar(product.rating, starSize = 12.dp)
                Spacer(Modifier.width(4.dp))
                Text(
                    text = String.format(Locale.US, "%.1f", product.rating),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
            Spacer(Modifier.height(6.dp))
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = product.price.formatPrice(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.ExtraBold
                )
                if (product.discountPercentage >= 1) {
                    Text(
                        text = product.originalPrice.formatPrice(),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextMuted,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingView() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "😕", fontSize = 48.sp)
        Spacer(Modifier.height(8.dp))
        Text(text = message, color = TextMuted)
        Spacer(Modifier.height(16.dp))
        Button(onClick = onRetry, shape = RoundedCornerShape(50)) {
            Icon(Icons.Filled.Refresh, contentDescription = null)
            Spacer(Modifier.width(6.dp))
            Text("Reintentar")
        }
    }
}