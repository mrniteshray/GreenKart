package com.greenkart.presentation.home

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.greenkart.domain.model.Category
import com.greenkart.domain.model.Vegetable
import com.greenkart.presentation.auth.AuthViewModel
import com.greenkart.presentation.home.HomeViewModel
import com.greenkart.presentation.components.CategoryItem
import com.greenkart.presentation.components.VegetableCard
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = koinViewModel(),
    authViewModel: AuthViewModel = koinViewModel(),
    cartViewModel: com.greenkart.presentation.cart.CartViewModel = koinViewModel(),
    onNavigateToDetail: (String) -> Unit,
    onViewCart: () -> Unit
) {
    val context = LocalContext.current
    val homeState by homeViewModel.homeState.collectAsState()
    val authState by authViewModel.authState.collectAsState()
    val cartState by cartViewModel.state
    val user = authState.user

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Top Header (Address and Profile)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Home",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = user?.address?.takeIf { it.isNotEmpty() } ?: "Add your address",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

            }

            // Search Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = homeState.searchQuery,
                    onValueChange = { homeViewModel.onSearchQueryChange(it) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search for vegetables...", color = Color.Gray) },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    },
                    trailingIcon = {
                        if (homeState.searchQuery.isNotEmpty()) {
                            IconButton(onClick = { homeViewModel.onSearchQueryChange("") }) {
                                Icon(
                                    Icons.Default.Clear,
                                    contentDescription = "Clear Search",
                                    tint = Color.Gray
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                        cursorColor = MaterialTheme.colorScheme.primary
                    ),
                    singleLine = true
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Categories
                item {
                    Text(
                        text = "What are you looking for?",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(homeState.categories) { category ->
                            CategoryItem(
                                category = category,
                                isSelected = homeState.selectedCategoryId == category.id,
                                onClick = {
                                    if (homeState.selectedCategoryId == category.id) {
                                        homeViewModel.selectCategory(null)
                                    } else {
                                        homeViewModel.selectCategory(category.id)
                                    }
                                }
                            )
                        }
                    }
                }

                // Featured/All Vegetables
                item {
                    Text(
                        text = if (homeState.selectedCategoryId == null) "Top Picks for You" else "Fresh in ${homeState.categories.find { it.id == homeState.selectedCategoryId }?.name}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                }

                val filteredVegetables = homeState.vegetables.filter { vegetable ->
                    val matchesCategory = homeState.selectedCategoryId == null || vegetable.categoryId == homeState.selectedCategoryId
                    val matchesSearch = vegetable.name.contains(homeState.searchQuery, ignoreCase = true) ||
                                       vegetable.description.contains(homeState.searchQuery, ignoreCase = true)
                    matchesCategory && matchesSearch
                }

                if (filteredVegetables.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Default.SearchOff,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No results found for \"${homeState.searchQuery}\"",
                                color = Color.Gray,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                items(filteredVegetables) { vegetable ->
                    VegetableCard(
                        vegetable = vegetable,
                        onClick = { onNavigateToDetail(vegetable.id) },
                        onAddClick = {
                            cartViewModel.addToCart(
                                com.greenkart.domain.model.CartItem(
                                    id = vegetable.id,
                                    name = vegetable.name,
                                    price = vegetable.price,
                                    unit = vegetable.unit,
                                    imageUrl = vegetable.imageUrl,
                                    quantity = 1
                                )
                            )
                        }
                    )
                }

                // Spacer for floating cart
                if (cartState.items.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }

        // Floating Cart Bar (Zomato Style)
        if (cartState.items.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(60.dp)
                    .clickable { onViewCart() },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(
                                    Color.White.copy(alpha = 0.2f),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${cartState.items.sumOf { it.quantity }} items",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "₹${cartState.totalPrice}",
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "View Cart",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForwardIos,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

