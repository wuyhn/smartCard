# SmartCard Application - USER and ADMIN Module Split

## 📖 Tổng Quan

Dự án này triển khai hệ thống SmartCard với 2 module riêng biệt cho USER (Khách hàng) và ADMIN (Quản trị viên).

## 🏗️ Cấu Trúc Dự Án

### 1. Kotlin Compose UI (Frontend)

#### `SmardCard/composeApp/src/jvmMain/kotlin/org/example/project/`

**App.kt** - Main application với navigation logic:
- `AppScreen` enum với các màn hình: CONNECT, ROLE_SELECTION, PIN_ENTRY, USER_MENU, ADMIN_MENU, etc.
- `SmartCardApp()` composable quản lý navigation và state
- Flow: Connect → Role Selection → PIN Entry → User/Admin Menu → Các chức năng cụ thể

#### `SmardCard/composeApp/src/jvmMain/kotlin/org/example/project/screen/`

**RoleSelectionScreen.kt** - Màn hình chọn vai trò:
- 2 cards lớn để chọn USER hoặc ADMIN
- USER card: Màu xanh dương `#5C6BC0`, icon 👤
- ADMIN card: Màu cam `#FF7043`, icon 👨‍💼
- Gradient background với 3 màu: `#FAFAFA`, `#F5F5F5`, `#E8EAF6`
- Rounded corners: 24dp, Elevation: 12dp
- Hover effect khi di chuột

**UserMainMenuScreen.kt** - Menu chính USER (6 chức năng):
1. 👁️ Xem thông tin cá nhân → Navigate to CustomerViewScreen
2. 💰 Kiểm tra số dư → Dialog hiển thị số dư
3. 🔑 Đổi mã PIN → Navigate to ChangePinScreen
4. 🎮 Xem danh sách game → Dialog hiển thị games
5. 🎫 Sử dụng vé chơi → Dialog nhập mã game và số vé
6. 🔙 Quay lại → Back to Role Selection

- Grid layout: 2 columns
- Card elevation: 8dp
- Rounded corners: 20dp

**AdminMainMenuScreen.kt** - Menu chính ADMIN (9 chức năng):
1. ✍️ Ghi thông tin khách hàng → Navigate to WriteDataScreen
2. 💳 Nạp tiền vào thẻ → Dialog nhập số tiền
3. 💰 Thực hiện thanh toán → Dialog nhập số tiền
4. 🎮 Thêm game mới → Dialog nhập mã game và số vé
5. 🔄 Cập nhật vé game → Dialog nhập mã game và số vé mới
6. 🗑️ Xóa game → Dialog nhập mã game
7. 🔓 Reset PIN counter → Confirm dialog
8. 👁️ Xem thông tin thẻ → Navigate to CustomerViewScreen
9. 🔙 Quay lại → Back to Role Selection

- Grid layout: 3 columns
- Card elevation: 8dp
- Rounded corners: 20dp

### 2. JavaCard Backend

#### `ParkCard/src/ParkCard/`

**CustomerCardApplet.java** - Applet chính:
- Quản lý thông tin khách hàng, số dư, PIN, vé game
- Tích hợp UserInterface và AdminInterface
- Method `process()` phân luồng lệnh cho 2 interfaces
- Tất cả business logic methods đều là `protected` để accessible từ interfaces

**UserInterface.java** - Module xử lý lệnh USER:
- Instruction codes: `0x10 - 0x4F`
- Commands:
  - `0x10`: READ_INFO - Đọc thông tin
  - `0x11`: READ_PHOTO - Đọc ảnh
  - `0x20`: VERIFY_PIN - Xác thực PIN
  - `0x24`: CHANGE_PIN - Đổi PIN
  - `0x30`: CHECK_BALANCE - Kiểm tra số dư
  - `0x40`: USE_GAME_TICKET - Sử dụng vé
  - `0x41`: VIEW_GAMES - Xem danh sách game
  - `0x42`: FIND_GAME - Tìm game

**AdminInterface.java** - Module xử lý lệnh ADMIN:
- Instruction codes: `0x70 - 0xAF`
- Commands:
  - `0x70`: WRITE_INFO - Ghi thông tin khách hàng
  - `0x71`: START_PHOTO - Bắt đầu ghi ảnh
  - `0x72`: WRITE_PHOTO_CHUNK - Ghi chunk ảnh
  - `0x73`: FINISH_PHOTO - Hoàn tất ghi ảnh
  - `0x80`: RECHARGE - Nạp tiền
  - `0x81`: MAKE_PAYMENT - Thanh toán
  - `0x90`: ADD_GAME - Thêm game
  - `0x91`: UPDATE_GAME - Cập nhật vé game
  - `0x92`: REMOVE_GAME - Xóa game
  - `0xA0`: RESET_PIN - Reset PIN counter

## 🎨 Design Guidelines

### Colors
- **Primary (User)**: `#5C6BC0` (Indigo)
- **Secondary (Admin)**: `#FF7043` (Deep Orange)
- **Success**: `#81C784` (Light Green)
- **Error**: `#E57373` (Light Red)
- **Background**: `#FAFAFA`, `#F5F5F5`, `#E8EAF6`
- **White**: `#FFFFFF`

### Typography
- **Title**: 28-32sp
- **Subtitle**: 16-20sp
- **Body**: 14sp
- **Small**: 12sp

### Spacing
- 8dp, 12dp, 16dp, 20dp, 24dp, 32dp

### Shapes
- **Rounded corners**: 20-24dp
- **Card elevation**: 8-12dp

## 🔄 Navigation Flow

```
[Connect Screen]
       ↓
[Role Selection Screen]
       ↓
   Choose Role
    ↙     ↘
USER       ADMIN
 ↓           ↓
[PIN Entry] [PIN Entry]
 ↓           ↓
[User Menu] [Admin Menu]
 ↓           ↓
Functions   Functions
```

### User Flow:
1. Connect Card
2. Select USER role
3. Enter PIN
4. Access User Menu (6 functions)
5. Perform actions or navigate to screens

### Admin Flow:
1. Connect Card
2. Select ADMIN role
3. Enter PIN
4. Access Admin Menu (9 functions)
5. Perform admin tasks or navigate to screens

## 📝 Implementation Notes

### Completed:
✅ RoleSelectionScreen.kt - Role selection with beautiful UI
✅ UserMainMenuScreen.kt - User menu with 6 functions
✅ AdminMainMenuScreen.kt - Admin menu with 9 functions
✅ UserInterface.java - USER instruction codes (0x10-0x4F)
✅ AdminInterface.java - ADMIN instruction codes (0x70-0xAF)
✅ CustomerCardApplet.java - Main applet with both interfaces
✅ App.kt - Navigation logic with new screens

### Architectural Decisions:
1. **Separation of Concerns**: USER và ADMIN commands được tách riêng vào 2 classes
2. **Protected Methods**: Tất cả business logic methods trong CustomerCardApplet là `protected` để accessible từ interfaces
3. **Clear Instruction Ranges**: 
   - USER: 0x10-0x4F
   - ADMIN: 0x70-0xAF
4. **Navigation State**: App.kt quản lý `isAdminMode` để biết user đang ở mode nào
5. **Dialogs**: Sử dụng dialogs cho quick actions, navigation cho complex flows

### Notes:
- Các placeholder screens (ConnectScreen, PinEntryScreen, WriteDataScreen, CustomerViewScreen, ChangePinScreen) cần được implement thực tế
- SmartCardManager cần được implement để giao tiếp với JavaCard
- FloatingBubbles animation component cần được thêm vào
- Build configuration (Gradle files) cần được setup cho Compose Multiplatform

## 🚀 Next Steps

1. Implement các màn hình còn lại (ConnectScreen, PinEntryScreen, etc.)
2. Implement SmartCardManager để giao tiếp với thẻ
3. Thêm FloatingBubbles animation component
4. Setup build configuration
5. Testing và validation
6. Security hardening

## 📦 Dependencies (To be added)

- Compose Multiplatform
- JavaCard SDK
- Smart Card IO libraries
- Testing frameworks

## 🔒 Security Considerations

- PIN verification trước khi thực hiện các operations
- Separate instruction ranges để ngăn USER execute ADMIN commands
- Input validation cho tất cả user inputs
- Proper error handling

---

**Version**: 1.0
**Author**: SmartCard Development Team
**Last Updated**: 2025-12-12
