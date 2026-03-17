package com.greenkart.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.greenkart.domain.model.CartItem
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: CartViewModel = koinViewModel(),
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit
) {
    val state by viewModel.state

    LaunchedEffect(state.orderPlaced) {
        if (state.orderPlaced) {
            onOrderPlaced()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Cart", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            if (state.items.isNotEmpty()) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shadowElevation = 8.dp,
                    color = Color.White
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Total Amount", fontSize = 16.sp, color = Color.Gray)
                            Text("₹${state.totalPrice}", fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color.Black)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { viewModel.placeOrder() },
                            modifier = Modifier.fillMaxWidth().height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isPlacingOrder
                        ) {
                            if (state.isPlacingOrder) {
                                CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            } else {
                                Text("Place Order", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    ) { padding ->
        if (state.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Your cart is empty", style = MaterialTheme.typography.headlineSmall, color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding).background(Color(0xFFF9F9F9)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.items) { item ->
                    CartItemRow(
                        item = item,
                        onUpdateQuantity = { qty -> viewModel.updateQuantity(item.id, qty) },
                        onRemove = { viewModel.removeFromCart(item) }
                    )
                }
            }
        }

        if (state.error.isNotEmpty()) {
            AlertDialog(
                onDismissRequest = { },
                title = { Text("Error") },
                text = { Text(state.error) },
                confirmButton = {
                    TextButton(onClick = { /* Dismiss error? viewModel should probably have a reset error function */ }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun CartItemRow(
    item: CartItem,
    onUpdateQuantity: (Int) -> Unit,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                loading = { Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp) } }
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("₹${item.price} / ${item.unit}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = { if (item.quantity > 1) onUpdateQuantity(item.quantity - 1) else onRemove() },
                        modifier = Modifier.size(32.dp).background(Color(0xFFF3F3F3), RoundedCornerShape(8.dp))
                    ) {
                        Icon(if (item.quantity > 1) Icons.Default.Remove else Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    }
                    Text("${item.quantity}", modifier = Modifier.padding(horizontal = 16.dp), fontWeight = FontWeight.Bold)
                    IconButton(
                        onClick = { onUpdateQuantity(item.quantity + 1) },
                        modifier = Modifier.size(32.dp).background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                    }
                }
            }
            
            Text("₹${item.price * item.quantity}", fontWeight = FontWeight.ExtraBold, fontSize = 16.sp, color = Color.Black)
        }
    }
}
