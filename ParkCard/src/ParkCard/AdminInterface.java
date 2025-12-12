package ParkCard;

import javacard.framework.*;

/**
 * Module xử lý các lệnh từ ADMIN (Quản trị viên)
 * 
 * Class này chịu trách nhiệm xử lý tất cả các lệnh APDU từ nhân viên quản lý,
 * bao gồm ghi thông tin, nạp tiền, thanh toán, quản lý game, và reset PIN.
 * 
 * Instruction codes: 0x70 - 0xAF
 */
public class AdminInterface {
    
    // Instruction codes cho ADMIN - Ghi thông tin
    public static final byte INS_ADMIN_WRITE_INFO = (byte) 0x70;
    public static final byte INS_ADMIN_START_PHOTO = (byte) 0x71;
    public static final byte INS_ADMIN_WRITE_PHOTO_CHUNK = (byte) 0x72;
    public static final byte INS_ADMIN_FINISH_PHOTO = (byte) 0x73;
    
    // Instruction codes cho ADMIN - Quản lý số dư
    public static final byte INS_ADMIN_RECHARGE = (byte) 0x80;
    public static final byte INS_ADMIN_MAKE_PAYMENT = (byte) 0x81;
    
    // Instruction codes cho ADMIN - Quản lý game
    public static final byte INS_ADMIN_ADD_GAME = (byte) 0x90;
    public static final byte INS_ADMIN_UPDATE_GAME = (byte) 0x91;
    public static final byte INS_ADMIN_REMOVE_GAME = (byte) 0x92;
    
    // Instruction codes cho ADMIN - Reset PIN
    public static final byte INS_ADMIN_RESET_PIN = (byte) 0xA0;
    
    // Reference đến CustomerCardApplet chính
    private CustomerCardApplet applet;
    
    /**
     * Constructor
     * 
     * @param applet Reference đến CustomerCardApplet instance
     */
    public AdminInterface(CustomerCardApplet applet) {
        this.applet = applet;
    }
    
    /**
     * Xử lý các lệnh APDU từ ADMIN
     * 
     * @param apdu APDU object chứa lệnh cần xử lý
     * @param ins Instruction byte
     * @return true nếu lệnh được xử lý thành công, false nếu không phải lệnh ADMIN
     */
    public boolean processCommand(APDU apdu, byte ins) {
        switch (ins) {
            // Ghi thông tin khách hàng
            case INS_ADMIN_WRITE_INFO:
                applet.writeCustomerInfo(apdu);
                return true;
            
            // Ghi ảnh (photo) theo chunks
            case INS_ADMIN_START_PHOTO:
                applet.startWritePhoto(apdu);
                return true;
            
            case INS_ADMIN_WRITE_PHOTO_CHUNK:
                applet.writePhotoChunk(apdu);
                return true;
            
            case INS_ADMIN_FINISH_PHOTO:
                applet.finishPhotoWrite(apdu);
                return true;
            
            // Quản lý số dư
            case INS_ADMIN_RECHARGE:
                applet.rechargeBalance(apdu);
                return true;
            
            case INS_ADMIN_MAKE_PAYMENT:
                applet.makePayment(apdu);
                return true;
            
            // Quản lý game
            case INS_ADMIN_ADD_GAME:
                // Thêm game mới hoặc tăng số vé
                applet.addOrIncreaseTickets(apdu);
                return true;
            
            case INS_ADMIN_UPDATE_GAME:
                // Cập nhật số vé của game
                applet.updateGameTickets(apdu);
                return true;
            
            case INS_ADMIN_REMOVE_GAME:
                // Xóa game khỏi hệ thống
                applet.removeGame(apdu);
                return true;
            
            // Reset PIN counter
            case INS_ADMIN_RESET_PIN:
                applet.resetPinCounter(apdu);
                return true;
            
            // Không phải lệnh ADMIN
            default:
                return false;
        }
    }
}
