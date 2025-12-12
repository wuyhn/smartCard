package org.example.project.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Menu chính cho chế độ ADMIN (Quản trị viên)
 * 
 * Cung cấp 9 chức năng chính cho nhân viên quản lý:
 * 1. Ghi thông tin khách hàng
 * 2. Nạp tiền vào thẻ
 * 3. Thực hiện thanh toán
 * 4. Thêm game mới
 * 5. Cập nhật vé game
 * 6. Xóa game
 * 7. Reset PIN counter
 * 8. Xem thông tin thẻ
 * 9. Quay lại
 *
 * @param smartCardManager Manager quản lý các thao tác với thẻ
 * @param onNavigateToWriteData Navigate đến màn hình ghi dữ liệu
 * @param onNavigateToCustomerView Navigate đến màn hình xem thông tin
 * @param onBack Quay lại màn hình chọn vai trò
 */
@Composable
fun AdminMainMenuScreen(
    smartCardManager: Any, // ISmartCardManager in actual implementation
    onNavigateToWriteData: () -> Unit,
    onNavigateToCustomerView: () -> Unit,
    onBack: () -> Unit
) {
    // Gradient background
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFAFAFA),
            Color(0xFFF5F5F5),
            Color(0xFFE8EAF6)
        )
    )

    // States cho các dialogs
    var showRechargeDialog by remember { mutableStateOf(false) }
    var showPaymentDialog by remember { mutableStateOf(false) }
    var showAddGameDialog by remember { mutableStateOf(false) }
    var showUpdateGameDialog by remember { mutableStateOf(false) }
    var showRemoveGameDialog by remember { mutableStateOf(false) }
    var showResetPinDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        // FloatingBubbles() - placeholder

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp)
        ) {
            // Header Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                shape = RoundedCornerShape(20.dp),
                elevation = 8.dp,
                backgroundColor = Color(0xFFFF7043) // Admin color
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👨‍💼",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Chế độ Quản trị",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Menu Items Grid (3 columns)
            val menuItems = listOf(
                AdminMenuItem(
                    icon = "✍️",
                    title = "Ghi thông tin",
                    description = "Ghi thông tin khách hàng",
                    color = Color(0xFF64B5F6),
                    onClick = onNavigateToWriteData
                ),
                AdminMenuItem(
                    icon = "💳",
                    title = "Nạp tiền",
                    description = "Nạp tiền vào thẻ",
                    color = Color(0xFF81C784),
                    onClick = { showRechargeDialog = true }
                ),
                AdminMenuItem(
                    icon = "💰",
                    title = "Thanh toán",
                    description = "Thực hiện thanh toán",
                    color = Color(0xFFFFB74D),
                    onClick = { showPaymentDialog = true }
                ),
                AdminMenuItem(
                    icon = "🎮",
                    title = "Thêm game",
                    description = "Thêm game mới",
                    color = Color(0xFFBA68C8),
                    onClick = { showAddGameDialog = true }
                ),
                AdminMenuItem(
                    icon = "🔄",
                    title = "Cập nhật vé",
                    description = "Cập nhật vé game",
                    color = Color(0xFF4DD0E1),
                    onClick = { showUpdateGameDialog = true }
                ),
                AdminMenuItem(
                    icon = "🗑️",
                    title = "Xóa game",
                    description = "Xóa game khỏi hệ thống",
                    color = Color(0xFFE57373),
                    onClick = { showRemoveGameDialog = true }
                ),
                AdminMenuItem(
                    icon = "🔓",
                    title = "Reset PIN",
                    description = "Reset PIN counter",
                    color = Color(0xFFFFD54F),
                    onClick = { showResetPinDialog = true }
                ),
                AdminMenuItem(
                    icon = "👁️",
                    title = "Xem thông tin",
                    description = "Xem thông tin thẻ",
                    color = Color(0xFF9575CD),
                    onClick = onNavigateToCustomerView
                ),
                AdminMenuItem(
                    icon = "🔙",
                    title = "Quay lại",
                    description = "Trở về màn hình chính",
                    color = Color(0xFF90A4AE),
                    onClick = onBack
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(menuItems) { item ->
                    AdminMenuItemCard(item)
                }
            }
        }

        // Dialogs
        if (showRechargeDialog) {
            RechargeDialog(
                onConfirm = { amount ->
                    // Logic nạp tiền (sẽ gọi smartCardManager)
                    showRechargeDialog = false
                },
                onDismiss = { showRechargeDialog = false }
            )
        }

        if (showPaymentDialog) {
            PaymentDialog(
                onConfirm = { amount ->
                    // Logic thanh toán (sẽ gọi smartCardManager)
                    showPaymentDialog = false
                },
                onDismiss = { showPaymentDialog = false }
            )
        }

        if (showAddGameDialog) {
            AddGameDialog(
                onConfirm = { gameCode, tickets ->
                    // Logic thêm game (sẽ gọi smartCardManager)
                    showAddGameDialog = false
                },
                onDismiss = { showAddGameDialog = false }
            )
        }

        if (showUpdateGameDialog) {
            UpdateGameDialog(
                onConfirm = { gameCode, newTickets ->
                    // Logic cập nhật vé (sẽ gọi smartCardManager)
                    showUpdateGameDialog = false
                },
                onDismiss = { showUpdateGameDialog = false }
            )
        }

        if (showRemoveGameDialog) {
            RemoveGameDialog(
                onConfirm = { gameCode ->
                    // Logic xóa game (sẽ gọi smartCardManager)
                    showRemoveGameDialog = false
                },
                onDismiss = { showRemoveGameDialog = false }
            )
        }

        if (showResetPinDialog) {
            ResetPinConfirmDialog(
                onConfirm = {
                    // Logic reset PIN counter (sẽ gọi smartCardManager)
                    showResetPinDialog = false
                },
                onDismiss = { showResetPinDialog = false }
            )
        }
    }
}

/**
 * Data class cho admin menu item
 */
private data class AdminMenuItem(
    val icon: String,
    val title: String,
    val description: String,
    val color: Color,
    val onClick: () -> Unit
)

/**
 * Component hiển thị một admin menu item card
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AdminMenuItemCard(item: AdminMenuItem) {
    Card(
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Text(
                text = item.icon,
                fontSize = 40.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Title box với màu
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = item.color,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = item.description,
                fontSize = 11.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

/**
 * Dialog nạp tiền
 */
@Composable
private fun RechargeDialog(
    onConfirm: (amount: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "💳 Nạp Tiền",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Số tiền (VNĐ)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            val value = amount.toIntOrNull() ?: 0
                            if (value > 0) {
                                onConfirm(value)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF7043)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog thanh toán
 */
@Composable
private fun PaymentDialog(
    onConfirm: (amount: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var amount by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "💰 Thanh Toán",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Số tiền (VNĐ)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            val value = amount.toIntOrNull() ?: 0
                            if (value > 0) {
                                onConfirm(value)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF7043)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog thêm game mới
 */
@Composable
private fun AddGameDialog(
    onConfirm: (gameCode: String, tickets: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var gameCode by remember { mutableStateOf("") }
    var tickets by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "🎮 Thêm Game Mới",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = gameCode,
                    onValueChange = { gameCode = it },
                    label = { Text("Mã game") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                
                OutlinedTextField(
                    value = tickets,
                    onValueChange = { tickets = it },
                    label = { Text("Số lượng vé") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            val ticketCount = tickets.toIntOrNull() ?: 0
                            if (gameCode.isNotBlank() && ticketCount > 0) {
                                onConfirm(gameCode, ticketCount)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF7043)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog cập nhật vé game
 */
@Composable
private fun UpdateGameDialog(
    onConfirm: (gameCode: String, newTickets: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var gameCode by remember { mutableStateOf("") }
    var newTickets by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "🔄 Cập Nhật Vé Game",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = gameCode,
                    onValueChange = { gameCode = it },
                    label = { Text("Mã game") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
                
                OutlinedTextField(
                    value = newTickets,
                    onValueChange = { newTickets = it },
                    label = { Text("Số vé mới") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            val ticketCount = newTickets.toIntOrNull() ?: 0
                            if (gameCode.isNotBlank() && ticketCount > 0) {
                                onConfirm(gameCode, ticketCount)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF7043)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog xóa game
 */
@Composable
private fun RemoveGameDialog(
    onConfirm: (gameCode: String) -> Unit,
    onDismiss: () -> Unit
) {
    var gameCode by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "🗑️ Xóa Game",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                OutlinedTextField(
                    value = gameCode,
                    onValueChange = { gameCode = it },
                    label = { Text("Mã game") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = {
                            if (gameCode.isNotBlank()) {
                                onConfirm(gameCode)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFE57373)
                        )
                    ) {
                        Text("Xóa", color = Color.White)
                    }
                }
            }
        }
    }
}

/**
 * Dialog xác nhận reset PIN counter
 */
@Composable
private fun ResetPinConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 400.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🔓",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Reset PIN Counter",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Text(
                    text = "Bạn có chắc chắn muốn reset bộ đếm PIN?",
                    fontSize = 14.sp,
                    color = Color(0xFF757575),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF90A4AE)
                        )
                    ) {
                        Text("Hủy", color = Color.White)
                    }
                    
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFFFF7043)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}
