package org.example.project

import androidx.compose.runtime.*
import org.example.project.screen.*

/**
 * Interface placeholder cho SmartCardManager
 * Sẽ được implement với logic thực tế khi tích hợp card reader
 */
interface ISmartCardManager {
    // Placeholder methods
    fun connect(): Boolean
    fun disconnect()
    fun verifyPin(pin: String): Boolean
    fun readData(): ByteArray?
    fun writeData(data: ByteArray): Boolean
}

/**
 * Placeholder implementation của SmartCardManager
 */
class PlaceholderSmartCardManager : ISmartCardManager {
    override fun connect() = true
    override fun disconnect() {}
    override fun verifyPin(pin: String) = true
    override fun readData() = null
    override fun writeData(data: ByteArray) = true
}

/**
 * Enum định nghĩa các màn hình trong ứng dụng
 */
enum class AppScreen {
    CONNECT,              // Màn hình kết nối thẻ
    ROLE_SELECTION,       // MỚI - Màn hình chọn vai trò USER/ADMIN
    PIN_ENTRY,            // Màn hình nhập PIN
    USER_MENU,            // MỚI - Menu chính cho USER
    ADMIN_MENU,           // MỚI - Menu chính cho ADMIN
    WriteDataScreen,      // Màn hình ghi dữ liệu
    CustomerDataScreen,   // Màn hình xem dữ liệu khách hàng
    ChangePinScreen       // Màn hình đổi PIN
}

/**
 * Component chính của ứng dụng SmartCard
 * 
 * Quản lý navigation giữa các màn hình và state của ứng dụng
 */
@Composable
fun SmartCardApp() {
    // State quản lý màn hình hiện tại
    var currentScreen by remember { mutableStateOf(AppScreen.CONNECT) }
    
    // State lưu vai trò người dùng (false = USER, true = ADMIN)
    var isAdminMode by remember { mutableStateOf(false) }
    
    // SmartCardManager instance với type-safe interface
    val smartCardManager: ISmartCardManager = remember { PlaceholderSmartCardManager() }
    
    // Navigation logic
    when (currentScreen) {
        // Màn hình kết nối thẻ
        AppScreen.CONNECT -> {
            // ConnectScreen(
            //     smartCardManager = smartCardManager,
            //     onConnected = {
            //         currentScreen = AppScreen.ROLE_SELECTION
            //     }
            // )
            
            // Placeholder - chuyển thẳng sang ROLE_SELECTION để demo
            LaunchedEffect(Unit) {
                currentScreen = AppScreen.ROLE_SELECTION
            }
        }
        
        // Màn hình chọn vai trò USER/ADMIN
        AppScreen.ROLE_SELECTION -> {
            RoleSelectionScreen(
                onRoleSelected = { isAdmin ->
                    isAdminMode = isAdmin
                    currentScreen = AppScreen.PIN_ENTRY
                }
            )
        }
        
        // Màn hình nhập PIN
        AppScreen.PIN_ENTRY -> {
            // PinEntryScreen(
            //     smartCardManager = smartCardManager,
            //     onPinVerified = {
            //         currentScreen = if (isAdminMode) {
            //             AppScreen.ADMIN_MENU
            //         } else {
            //             AppScreen.USER_MENU
            //         }
            //     },
            //     onBack = {
            //         currentScreen = AppScreen.ROLE_SELECTION
            //     }
            // )
            
            // Placeholder - chuyển thẳng sang menu tương ứng để demo
            LaunchedEffect(Unit) {
                currentScreen = if (isAdminMode) {
                    AppScreen.ADMIN_MENU
                } else {
                    AppScreen.USER_MENU
                }
            }
        }
        
        // Menu chính cho USER
        AppScreen.USER_MENU -> {
            UserMainMenuScreen(
                smartCardManager = smartCardManager,
                onNavigateToCustomerView = {
                    currentScreen = AppScreen.CustomerDataScreen
                },
                onNavigateToChangePin = {
                    currentScreen = AppScreen.ChangePinScreen
                },
                onBack = {
                    currentScreen = AppScreen.ROLE_SELECTION
                }
            )
        }
        
        // Menu chính cho ADMIN
        AppScreen.ADMIN_MENU -> {
            AdminMainMenuScreen(
                smartCardManager = smartCardManager,
                onNavigateToWriteData = {
                    currentScreen = AppScreen.WriteDataScreen
                },
                onNavigateToCustomerView = {
                    currentScreen = AppScreen.CustomerDataScreen
                },
                onBack = {
                    currentScreen = AppScreen.ROLE_SELECTION
                }
            )
        }
        
        // Màn hình ghi dữ liệu (ADMIN only)
        AppScreen.WriteDataScreen -> {
            // WriteDataScreen(
            //     smartCardManager = smartCardManager,
            //     onBack = {
            //         currentScreen = AppScreen.ADMIN_MENU
            //     }
            // )
            
            // Placeholder
            PlaceholderScreen(
                title = "Write Data Screen",
                onBack = {
                    currentScreen = AppScreen.ADMIN_MENU
                }
            )
        }
        
        // Màn hình xem dữ liệu khách hàng
        AppScreen.CustomerDataScreen -> {
            // CustomerViewScreen(
            //     smartCardManager = smartCardManager,
            //     onBack = {
            //         currentScreen = if (isAdminMode) {
            //             AppScreen.ADMIN_MENU
            //         } else {
            //             AppScreen.USER_MENU
            //         }
            //     }
            // )
            
            // Placeholder
            PlaceholderScreen(
                title = "Customer View Screen",
                onBack = {
                    currentScreen = if (isAdminMode) {
                        AppScreen.ADMIN_MENU
                    } else {
                        AppScreen.USER_MENU
                    }
                }
            )
        }
        
        // Màn hình đổi PIN
        AppScreen.ChangePinScreen -> {
            // ChangePinScreen(
            //     smartCardManager = smartCardManager,
            //     onBack = {
            //         currentScreen = AppScreen.USER_MENU
            //     }
            // )
            
            // Placeholder
            PlaceholderScreen(
                title = "Change PIN Screen",
                onBack = {
                    currentScreen = AppScreen.USER_MENU
                }
            )
        }
    }
}

/**
 * Placeholder screen cho các màn hình chưa được implement
 */
@Composable
private fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit
) {
    androidx.compose.foundation.layout.Box(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        androidx.compose.foundation.layout.Column(
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ) {
            androidx.compose.material.Text(
                text = title,
                fontSize = 24.sp
            )
            androidx.compose.foundation.layout.Spacer(
                modifier = androidx.compose.ui.Modifier.height(16.dp)
            )
            androidx.compose.material.Button(onClick = onBack) {
                androidx.compose.material.Text("Back")
            }
        }
    }
}
