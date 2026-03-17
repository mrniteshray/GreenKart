package com.greenkart.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.greenkart.domain.model.Order
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreen(
    viewModel: OrderViewModel = koinViewModel()
) {
    val state by viewModel.state
    var selectedOrder by remember { mutableStateOf<Order?>(null) }
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9F9F9))
    ) {
        Text(
            text = "My Orders",
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )

        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (state.orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No orders placed yet", color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.orders.sortedByDescending { it.timestamp }) { order ->
                    OrderCard(
                        order = order,
                        onClick = {
                            selectedOrder = order
                            showBottomSheet = true
                        }
                    )
                }
            }
        }
    }

    if (showBottomSheet && selectedOrder != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color.White,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            OrderDetailContent(order = selectedOrder!!)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun OrderCard(order: Order, onClick: () -> Unit) {
    val date = remember(order.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(Date(order.timestamp))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Order #${order.id.takeLast(6).uppercase()}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(date, fontSize = 12.sp, color = Color.Gray)
                }
                Box(
                    modifier = Modifier
                        .background(Color(0xFFE3F2FD), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        order.status,
                        color = Color(0xFF1976D2),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            order.items.take(2).forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.quantity}x ${item.name}", fontSize = 14.sp)
                    Text(
                        "₹${item.price * item.quantity}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            if (order.items.size > 2) {
                Text(
                    "+ ${order.items.size - 2} more items",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Divider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Total Amount", fontWeight = FontWeight.Bold)
                Text(
                    "₹${order.totalPrice}",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun OrderDetailContent(order: Order) {
    val date = remember(order.timestamp) {
        val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
        sdf.format(Date(order.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "Order Details",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    "Order #${order.id.uppercase()}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Text(date, fontSize = 12.sp, color = Color.Gray)
            }
            Box(
                modifier = Modifier
                    .background(Color(0xFF2E7D32), RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    order.status,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Items", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))

        order.items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFF3F3F3), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "${item.quantity}", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = item.name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                        Text(text = item.unit, fontSize = 11.sp, color = Color.Gray)
                    }
                }
                Text(
                    text = "₹${item.price * item.quantity}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Text(text = "Delivery Address", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = if (order.deliveryAddress.isEmpty()) "No address provided" else order.deliveryAddress,
            fontSize = 14.sp,
            color = Color.DarkGray
        )

        Divider(modifier = Modifier.padding(vertical = 16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Bill Total", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(
                text = "₹${order.totalPrice}",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
