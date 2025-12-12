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
 * Menu chính cho chế độ USER (Khách hàng)
 * 
 * Cung cấp 6 chức năng chính cho khách hàng:
 * 1. Xem thông tin cá nhân
 * 2. Kiểm tra số dư
 * 3. Đổi mã PIN
 * 4. Xem danh sách game
 * 5. Sử dụng vé chơi
 * 6. Quay lại
 *
 * @param smartCardManager Manager quản lý các thao tác với thẻ (placeholder)
 * @param onNavigateToCustomerView Navigate đến màn hình xem thông tin
 * @param onNavigateToChangePin Navigate đến màn hình đổi PIN
 * @param onBack Quay lại màn hình chọn vai trò
 */
@Composable
fun UserMainMenuScreen(
    smartCardManager: Any, // SmartCardManager - placeholder type
    onNavigateToCustomerView: () -> Unit,
    onNavigateToChangePin: () -> Unit,
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
    var showBalanceDialog by remember { mutableStateOf(false) }
    var showGamesDialog by remember { mutableStateOf(false) }
    var showUseTicketDialog by remember { mutableStateOf(false) }
    
    // Sample data (sẽ được lấy từ smartCardManager trong thực tế)
    var balance by remember { mutableStateOf(100000) }
    var games by remember { mutableStateOf(listOf("Game A - 5 vé", "Game B - 3 vé", "Game C - 10 vé")) }

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
                backgroundColor = Color(0xFF5C6BC0) // User color
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "👤",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text(
                        text = "Chế độ Khách hàng",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            // Menu Items Grid (2 columns)
            val menuItems = listOf(
                MenuItem(
                    icon = "👁️",
                    title = "Xem thông tin",
                    description = "Xem thông tin cá nhân",
                    color = Color(0xFF64B5F6),
                    onClick = onNavigateToCustomerView
                ),
                MenuItem(
                    icon = "💰",
                    title = "Kiểm tra số dư",
                    description = "Xem số dư hiện tại",
                    color = Color(0xFF81C784),
                    onClick = { showBalanceDialog = true }
                ),
                MenuItem(
                    icon = "🔑",
                    title = "Đổi mã PIN",
                    description = "Thay đổi mã PIN",
                    color = Color(0xFFFFB74D),
                    onClick = onNavigateToChangePin
                ),
                MenuItem(
                    icon = "🎮",
                    title = "Danh sách game",
                    description = "Xem các game có sẵn",
                    color = Color(0xFFBA68C8),
                    onClick = { showGamesDialog = true }
                ),
                MenuItem(
                    icon = "🎫",
                    title = "Sử dụng vé",
                    description = "Sử dụng vé chơi game",
                    color = Color(0xFFE57373),
                    onClick = { showUseTicketDialog = true }
                ),
                MenuItem(
                    icon = "🔙",
                    title = "Quay lại",
                    description = "Trở về màn hình chính",
                    color = Color(0xFF90A4AE),
                    onClick = onBack
                )
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(menuItems) { item ->
                    MenuItemCard(item)
                }
            }
        }

        // Dialogs
        if (showBalanceDialog) {
            BalanceDialog(
                balance = balance,
                onDismiss = { showBalanceDialog = false }
            )
        }

        if (showGamesDialog) {
            GamesListDialog(
                games = games,
                onDismiss = { showGamesDialog = false }
            )
        }

        if (showUseTicketDialog) {
            UseTicketDialog(
                onConfirm = { gameCode, ticketCount ->
                    // Logic sử dụng vé (sẽ gọi smartCardManager)
                    showUseTicketDialog = false
                },
                onDismiss = { showUseTicketDialog = false }
            )
        }
    }
}

/**
 * Data class cho menu item
 */
private data class MenuItem(
    val icon: String,
    val title: String,
    val description: String,
    val color: Color,
    val onClick: () -> Unit
)

/**
 * Component hiển thị một menu item card
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun MenuItemCard(item: MenuItem) {
    Card(
        onClick = item.onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = 8.dp,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon
            Text(
                text = item.icon,
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 12.dp)
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
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = item.description,
                fontSize = 12.sp,
                color = Color(0xFF757575)
            )
        }
    }
}

/**
 * Dialog hiển thị số dư
 */
@Composable
private fun BalanceDialog(
    balance: Int,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = 8.dp
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "💰",
                    fontSize = 48.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Số dư hiện tại",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "${balance.toString().reversed().chunked(3).joinToString(".").reversed()} VNĐ",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF81C784),
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF5C6BC0)
                    )
                ) {
                    Text("Đóng", color = Color.White)
                }
            }
        }
    }
}

/**
 * Dialog hiển thị danh sách game
 */
@Composable
private fun GamesListDialog(
    games: List<String>,
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
                    .widthIn(max = 400.dp)
            ) {
                Text(
                    text = "🎮 Danh sách Game",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                games.forEach { game ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = 2.dp,
                        backgroundColor = Color(0xFFF5F5F5)
                    ) {
                        Text(
                            text = game,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF5C6BC0)
                    )
                ) {
                    Text("Đóng", color = Color.White)
                }
            }
        }
    }
}

/**
 * Dialog để sử dụng vé chơi game
 */
@Composable
private fun UseTicketDialog(
    onConfirm: (gameCode: String, ticketCount: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var gameCode by remember { mutableStateOf("") }
    var ticketCount by remember { mutableStateOf("") }

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
                    text = "🎫 Sử dụng Vé",
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
                    value = ticketCount,
                    onValueChange = { ticketCount = it },
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
                            val count = ticketCount.toIntOrNull() ?: 0
                            if (gameCode.isNotBlank() && count > 0) {
                                onConfirm(gameCode, count)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF5C6BC0)
                        )
                    ) {
                        Text("Xác nhận", color = Color.White)
                    }
                }
            }
        }
    }
}
