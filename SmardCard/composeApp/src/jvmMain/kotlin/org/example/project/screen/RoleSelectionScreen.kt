package org.example.project.screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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

/**
 * Màn hình chọn vai trò sử dụng cho hệ thống SmartCard
 * 
 * Screen này cho phép người dùng chọn giữa 2 chế độ:
 * - USER MODE: Dành cho khách hàng
 * - ADMIN MODE: Dành cho nhân viên quản lý
 *
 * @param onRoleSelected Callback được gọi khi người dùng chọn vai trò
 *                       - true: Admin mode
 *                       - false: User mode
 */
@Composable
fun RoleSelectionScreen(
    onRoleSelected: (isAdmin: Boolean) -> Unit
) {
    // Gradient background colors giống ConnectScreen
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFAFAFA),
            Color(0xFFF5F5F5),
            Color(0xFFE8EAF6)
        )
    )

    // Màu cho các card
    val userColor = Color(0xFF5C6BC0)  // Xanh dương cho USER
    val adminColor = Color(0xFFFF7043) // Cam cho ADMIN

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = backgroundGradient)
    ) {
        // FloatingBubbles animation (placeholder - cần component thực tế)
        // FloatingBubbles()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header với icon và title
            Text(
                text = "🎭",
                fontSize = 48.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Chọn Chế độ Sử dụng",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF37474F),
                modifier = Modifier.padding(bottom = 48.dp)
            )

            // Row chứa 2 cards để chọn
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(32.dp, Alignment.CenterHorizontally)
            ) {
                // USER Card
                RoleCard(
                    icon = "👤",
                    title = "Chế độ Khách hàng",
                    description = "Dành cho khách hàng sử dụng dịch vụ",
                    color = userColor,
                    onClick = { onRoleSelected(false) }
                )

                // ADMIN Card
                RoleCard(
                    icon = "👨‍💼",
                    title = "Chế độ Quản trị",
                    description = "Dành cho nhân viên quản lý",
                    color = adminColor,
                    onClick = { onRoleSelected(true) }
                )
            }
        }
    }
}

/**
 * Component Card để chọn vai trò
 * 
 * @param icon Icon emoji hiển thị
 * @param title Tiêu đề của card
 * @param description Mô tả ngắn
 * @param color Màu chủ đạo của card
 * @param onClick Callback khi click vào card
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun RoleCard(
    icon: String,
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    // State để xử lý hover effect
    var isHovered by remember { mutableStateOf(false) }
    
    // Animation cho hover effect
    val elevation by animateDpAsState(
        targetValue = if (isHovered) 16.dp else 12.dp,
        animationSpec = tween(durationMillis = 200)
    )
    
    val scale by animateFloatAsState(
        targetValue = if (isHovered) 1.05f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    Card(
        onClick = onClick,
        modifier = Modifier
            .width(280.dp)
            .height(320.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = elevation,
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Icon lớn ở trên
            Text(
                text = icon,
                fontSize = 72.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Box màu chứa title
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = color,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF616161),
                modifier = Modifier.padding(horizontal = 8.dp)
            )
        }
    }
}
