package appxyz.greenkart.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import appxyz.greenkart.presentation.account.AccountScreen
import appxyz.greenkart.presentation.home.HomeScreen

sealed class BottomNavItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    data object Shop : BottomNavItem("Shop", Icons.Filled.Home, Icons.Outlined.Home)
    data object MyList : BottomNavItem("My List", Icons.Filled.Favorite, Icons.Outlined.FavoriteBorder)
    data object Orders : BottomNavItem("Orders", Icons.Filled.ShoppingCart, Icons.Outlined.ShoppingCart)
    data object Account : BottomNavItem("Account", Icons.Filled.Person, Icons.Outlined.Person)
}

@Composable
fun MainScreen(
    onLogout: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onViewCart: () -> Unit
) {
    val items = listOf(
        BottomNavItem.Shop,
        BottomNavItem.MyList,
        BottomNavItem.Orders,
        BottomNavItem.Account
    )
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.height(90.dp)
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index },
                        label = { Text(item.title, fontWeight = FontWeight.Bold, fontSize = 12.sp) },
                        icon = {
                            Icon(
                                imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.title,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedItemIndex) {
                0 -> {
                    HomeScreen(
                        onNavigateToDetail = onNavigateToDetail,
                        onViewCart = onViewCart
                    )
                }
                1 -> {
                    appxyz.greenkart.presentation.favorite.MyListScreen(
                        onNavigateToDetail = onNavigateToDetail,
                        onViewCart = onViewCart
                    )
                }
                2 -> {
                    appxyz.greenkart.presentation.order.OrderScreen()
                }
                3 -> {
                    AccountScreen(onLogout = onLogout)
                }
                else -> {
                    val currentItem = items[selectedItemIndex]
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "${currentItem.title} Screen (Coming Soon)",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

