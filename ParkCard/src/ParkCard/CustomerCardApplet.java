package ParkCard;

import javacard.framework.*;

/**
 * CustomerCardApplet - Applet chính quản lý thẻ khách hàng
 * 
 * Applet này quản lý thông tin khách hàng, số dư, PIN, và vé game.
 * Các lệnh được xử lý thông qua 2 interface: UserInterface và AdminInterface
 */
public class CustomerCardApplet extends Applet {
    
    // Instances của UserInterface và AdminInterface
    private UserInterface userInterface;
    private AdminInterface adminInterface;
    
    // PIN object
    private OwnerPIN pin;
    
    // Arrays lưu trữ dữ liệu
    private byte[] customerInfo;
    private byte[] photoData;
    private byte[] gamesData;
    
    // Biến lưu số dư
    private short balance;
    
    // Constants
    private static final byte PIN_TRY_LIMIT = 3;
    private static final byte PIN_MAX_SIZE = 8;
    private static final short CUSTOMER_INFO_SIZE = 256;
    private static final short PHOTO_DATA_SIZE = 2048;
    private static final short GAMES_DATA_SIZE = 512;
    
    /**
     * Constructor - Khởi tạo applet
     */
    private CustomerCardApplet() {
        // Khởi tạo PIN với mặc định là "1234"
        pin = new OwnerPIN(PIN_TRY_LIMIT, PIN_MAX_SIZE);
        byte[] defaultPin = {(byte)'1', (byte)'2', (byte)'3', (byte)'4'};
        pin.update(defaultPin, (short)0, (byte)4);
        
        // Khởi tạo arrays
        customerInfo = new byte[CUSTOMER_INFO_SIZE];
        photoData = new byte[PHOTO_DATA_SIZE];
        gamesData = new byte[GAMES_DATA_SIZE];
        
        // Khởi tạo số dư
        balance = 0;
        
        // Khởi tạo 2 interface modules
        userInterface = new UserInterface(this);
        adminInterface = new AdminInterface(this);
    }
    
    /**
     * Install method
     */
    public static void install(byte[] bArray, short bOffset, byte bLength) {
        new CustomerCardApplet().register(bArray, (short) (bOffset + 1), bArray[bOffset]);
    }
    
    /**
     * Process method - Xử lý các lệnh APDU
     */
    public void process(APDU apdu) {
        if (selectingApplet()) {
            return;
        }
        
        byte[] buf = apdu.getBuffer();
        byte ins = buf[ISO7816.OFFSET_INS];
        
        // Thử xử lý bằng User Module trước
        if (userInterface.processCommand(apdu, ins)) {
            return;
        }
        
        // Nếu không phải user command, thử Admin Module
        if (adminInterface.processCommand(apdu, ins)) {
            return;
        }
        
        // Nếu cả 2 đều không xử lý được
        ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
    }
    
    // ===== PROTECTED METHODS - Được gọi từ UserInterface và AdminInterface =====
    
    /**
     * Ghi thông tin khách hàng
     */
    protected void writeCustomerInfo(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short lc = (short)(buffer[ISO7816.OFFSET_LC] & 0xFF);
        short bytesRead = apdu.setIncomingAndReceive();
        
        // Copy dữ liệu vào customerInfo array
        Util.arrayCopy(buffer, ISO7816.OFFSET_CDATA, customerInfo, (short)0, lc);
    }
    
    /**
     * Bắt đầu ghi ảnh
     */
    protected void startWritePhoto(APDU apdu) {
        // Reset photo data
        Util.arrayFillNonAtomic(photoData, (short)0, PHOTO_DATA_SIZE, (byte)0);
    }
    
    /**
     * Ghi chunk của ảnh
     */
    protected void writePhotoChunk(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short offset = Util.getShort(buffer, ISO7816.OFFSET_CDATA);
        short length = Util.getShort(buffer, (short)(ISO7816.OFFSET_CDATA + 2));
        
        // Copy chunk vào photoData
        Util.arrayCopy(buffer, (short)(ISO7816.OFFSET_CDATA + 4), photoData, offset, length);
    }
    
    /**
     * Hoàn tất ghi ảnh
     */
    protected void finishPhotoWrite(APDU apdu) {
        // Placeholder - có thể thêm validation ở đây
    }
    
    /**
     * Đọc tất cả dữ liệu khách hàng
     */
    protected void readAllData(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        
        // Copy customerInfo vào buffer
        Util.arrayCopy(customerInfo, (short)0, buffer, (short)0, CUSTOMER_INFO_SIZE);
        apdu.setOutgoingAndSend((short)0, CUSTOMER_INFO_SIZE);
    }
    
    /**
     * Đọc chunk của ảnh
     */
    protected void readPhotoChunk(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short offset = Util.getShort(buffer, ISO7816.OFFSET_CDATA);
        short length = Util.getShort(buffer, (short)(ISO7816.OFFSET_CDATA + 2));
        
        // Copy chunk từ photoData vào buffer
        Util.arrayCopy(photoData, offset, buffer, (short)0, length);
        apdu.setOutgoingAndSend((short)0, length);
    }
    
    /**
     * Xác thực PIN
     */
    protected void verifyPIN(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte pinLength = buffer[ISO7816.OFFSET_LC];
        
        if (pin.check(buffer, ISO7816.OFFSET_CDATA, pinLength)) {
            // PIN đúng
            return;
        } else {
            ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
    }
    
    /**
     * Đổi PIN
     */
    protected void changePIN(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        byte oldPinLength = buffer[ISO7816.OFFSET_CDATA];
        byte newPinLength = buffer[(short)(ISO7816.OFFSET_CDATA + 1 + oldPinLength)];
        
        // Verify old PIN
        if (!pin.check(buffer, (short)(ISO7816.OFFSET_CDATA + 1), oldPinLength)) {
            ISOException.throwIt(ISO7816.SW_SECURITY_STATUS_NOT_SATISFIED);
        }
        
        // Update to new PIN
        pin.update(buffer, (short)(ISO7816.OFFSET_CDATA + 2 + oldPinLength), newPinLength);
    }
    
    /**
     * Kiểm tra số dư
     */
    protected void checkBalance(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        Util.setShort(buffer, (short)0, balance);
        apdu.setOutgoingAndSend((short)0, (short)2);
    }
    
    /**
     * Nạp tiền vào thẻ
     */
    protected void rechargeBalance(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short amount = Util.getShort(buffer, ISO7816.OFFSET_CDATA);
        
        // Tăng số dư
        balance = (short)(balance + amount);
    }
    
    /**
     * Thực hiện thanh toán
     */
    protected void makePayment(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        short amount = Util.getShort(buffer, ISO7816.OFFSET_CDATA);
        
        // Kiểm tra số dư đủ không
        if (balance < amount) {
            ISOException.throwIt(ISO7816.SW_CONDITIONS_NOT_SATISFIED);
        }
        
        // Trừ số dư
        balance = (short)(balance - amount);
    }
    
    /**
     * Thêm game mới hoặc tăng số vé
     */
    protected void addOrIncreaseTickets(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        // Game code và số vé trong CDATA
        // Placeholder implementation
    }
    
    /**
     * Giảm số vé của game (khi user sử dụng)
     */
    protected void decreaseGameTickets(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        // Game code và số vé cần giảm trong CDATA
        // Placeholder implementation
    }
    
    /**
     * Đọc danh sách games
     */
    protected void readGames(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        Util.arrayCopy(gamesData, (short)0, buffer, (short)0, GAMES_DATA_SIZE);
        apdu.setOutgoingAndSend((short)0, GAMES_DATA_SIZE);
    }
    
    /**
     * Cập nhật số vé của game
     */
    protected void updateGameTickets(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        // Game code và số vé mới trong CDATA
        // Placeholder implementation
    }
    
    /**
     * Tìm game theo mã
     */
    protected void findGame(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        // Game code trong CDATA
        // Trả về thông tin game nếu tìm thấy
        // Placeholder implementation
    }
    
    /**
     * Xóa game
     */
    protected void removeGame(APDU apdu) {
        byte[] buffer = apdu.getBuffer();
        // Game code trong CDATA
        // Placeholder implementation
    }
    
    /**
     * Reset PIN counter
     */
    protected void resetPinCounter(APDU apdu) {
        pin.resetAndUnblock();
    }
}
