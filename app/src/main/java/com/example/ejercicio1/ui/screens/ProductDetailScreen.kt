package com.example.ejercicio1.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.ejercicio1.data.model.Product
import com.example.ejercicio1.data.model.Review
import com.example.ejercicio1.ui.components.DiscountBadge
import com.example.ejercicio1.ui.components.ErrorView
import com.example.ejercicio1.ui.components.LoadingView
import com.example.ejercicio1.ui.components.RatingBar
import com.example.ejercicio1.ui.components.formatPrice
import com.example.ejercicio1.ui.theme.Background
import com.example.ejercicio1.ui.theme.Coral
import com.example.ejercicio1.ui.theme.Mint
import com.example.ejercicio1.ui.theme.TextMuted
import com.example.ejercicio1.ui.viewmodel.ProductDetailUiState
import com.example.ejercicio1.ui.viewmodel.ProductDetailViewModel
import java.util.Locale

@Composable
fun ProductDetailScreen(
    productId: Int,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.load(productId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val s = state) {
            is ProductDetailUiState.Loading -> LoadingView()
            is ProductDetailUiState.Error -> ErrorView(s.message) { viewModel.load(productId) }
            is ProductDetailUiState.Success -> ProductDetailContent(s.product)
        }

        IconButton(
            onClick = onBack,
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White),
            modifier = Modifier
                .statusBarsPadding()
                .padding(12.dp)
        ) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
    }
}

@Composable
private fun ProductDetailContent(product: Product) {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            item { ImageGallery(product) }

            item {
                Column(
                    modifier = Modifier
                        .offset(y = (-24).dp)
                        .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp)
                ) {
                    Text(
                        text = product.category.uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )
                    product.brand?.let {
                        Text(text = "por $it", color = TextMuted)
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RatingBar(product.rating, starSize = 18.dp)
                        Spacer(Modifier.width(6.dp))
                        Text(
                            text = String.format(Locale.US, "%.2f", product.rating) +
                                    " (${product.reviews?.size ?: 0} reseñas)",
                            color = TextMuted,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = product.price.formatPrice(),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.ExtraBold
                        )
                        if (product.discountPercentage >= 1) {
                            Text(
                                text = product.originalPrice.formatPrice(),
                                color = TextMuted,
                                textDecoration = TextDecoration.LineThrough
                            )
                            DiscountBadge(product.discountPercentage)
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    StockBadge(product)

                    SectionTitle("Descripción")
                    Text(text = product.description, color = TextMuted)

                    if (!product.tags.isNullOrEmpty()) {
                        Spacer(Modifier.height(12.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(product.tags) { tag ->
                                AssistChip(onClick = {}, label = { Text("#$tag") })
                            }
                        }
                    }

                    SectionTitle("Información")
                    InfoRow(Icons.Filled.Build, "Garantía", product.warrantyInformation)
                    InfoRow(Icons.Outlined.Info, "Envío", product.shippingInformation)
                    InfoRow(Icons.Filled.Refresh, "Devolución", product.returnPolicy)

                    if (!product.reviews.isNullOrEmpty()) {
                        SectionTitle("Reseñas")
                        product.reviews.forEach { review ->
                            ReviewCard(review)
                            Spacer(Modifier.height(8.dp))
                        }
                    }
                }
            }
        }

        Surface(shadowElevation = 12.dp) {
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        "${product.title} agregado al carrito",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Agregar al carrito", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ImageGallery(product: Product) {
    val images = product.images.orEmpty().ifEmpty { listOf(product.thumbnail) }
    val pagerState = rememberPagerState(pageCount = { images.size })

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(360.dp)
            .background(Brush.verticalGradient(listOf(Color(0xFFE0E7FF), Background)))
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            AsyncImage(
                model = images[page],
                contentDescription = product.title,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(top = 32.dp, bottom = 40.dp, start = 24.dp, end = 24.dp)
            )
        }
        if (images.size > 1) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 36.dp)
            ) {
                repeat(images.size) { index ->
                    val selected = pagerState.currentPage == index
                    Box(
                        Modifier
                            .size(width = if (selected) 20.dp else 8.dp, height = 8.dp)
                            .clip(CircleShape)
                            .background(
                                if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun StockBadge(product: Product) {
    val color = if (product.stock > 10) Mint else Coral
    Text(
        text = "${product.availabilityStatus ?: "Stock"} · ${product.stock} unidades",
        color = color,
        fontWeight = FontWeight.SemiBold,
        style = MaterialTheme.typography.labelLarge,
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    )
}

@Composable
private fun SectionTitle(text: String) {
    Spacer(Modifier.height(20.dp))
    HorizontalDivider(color = Background)
    Spacer(Modifier.height(12.dp))
    Text(text = text, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
    Spacer(Modifier.height(6.dp))
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, value: String?) {
    if (value == null) return
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
        ) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        }
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelMedium, color = TextMuted)
            Text(value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun ReviewCard(review: Review) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Background),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        text = review.reviewerName.take(1),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(Modifier.width(8.dp))
                Text(
                    text = review.reviewerName,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null,
                    tint = Color(0xFFFFB020),
                    modifier = Modifier.size(16.dp)
                )
                Text(text = review.rating.toString(), fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            Text(text = review.comment, color = TextMuted)
        }
    }
}