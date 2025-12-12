package ParkCard;

import javacard.framework.*;

/**
 * Module xử lý các lệnh từ USER (Khách hàng)
 * 
 * Class này chịu trách nhiệm xử lý tất cả các lệnh APDU từ người dùng cuối,
 * bao gồm đọc thông tin, xác thực PIN, kiểm tra số dư, và sử dụng vé game.
 * 
 * Instruction codes: 0x10 - 0x4F
 */
public class UserInterface {
    
    // Instruction codes cho USER - Đọc thông tin
    public static final byte INS_USER_READ_INFO = (byte) 0x10;
    public static final byte INS_USER_READ_PHOTO = (byte) 0x11;
    
    // Instruction codes cho USER - Xác thực và đổi PIN
    public static final byte INS_USER_VERIFY_PIN = (byte) 0x20;
    public static final byte INS_USER_CHANGE_PIN = (byte) 0x24;
    
    // Instruction codes cho USER - Quản lý số dư
    public static final byte INS_USER_CHECK_BALANCE = (byte) 0x30;
    
    // Instruction codes cho USER - Quản lý game tickets
    public static final byte INS_USER_USE_GAME_TICKET = (byte) 0x40;
    public static final byte INS_USER_VIEW_GAMES = (byte) 0x41;
    public static final byte INS_USER_FIND_GAME = (byte) 0x42;
    
    // Reference đến CustomerCardApplet chính
    private CustomerCardApplet applet;
    
    /**
     * Constructor
     * 
     * @param applet Reference đến CustomerCardApplet instance
     */
    public UserInterface(CustomerCardApplet applet) {
        this.applet = applet;
    }
    
    /**
     * Xử lý các lệnh APDU từ USER
     * 
     * @param apdu APDU object chứa lệnh cần xử lý
     * @param ins Instruction byte
     * @return true nếu lệnh được xử lý thành công, false nếu không phải lệnh USER
     */
    public boolean processCommand(APDU apdu, byte ins) {
        switch (ins) {
            // Đọc thông tin
            case INS_USER_READ_INFO:
                applet.readAllData(apdu);
                return true;
            
            case INS_USER_READ_PHOTO:
                applet.readPhotoChunk(apdu);
                return true;
            
            // Xác thực và đổi PIN
            case INS_USER_VERIFY_PIN:
                applet.verifyPIN(apdu);
                return true;
            
            case INS_USER_CHANGE_PIN:
                applet.changePIN(apdu);
                return true;
            
            // Quản lý số dư
            case INS_USER_CHECK_BALANCE:
                applet.checkBalance(apdu);
                return true;
            
            // Quản lý game tickets
            case INS_USER_USE_GAME_TICKET:
                // Sử dụng vé = giảm số vé
                applet.decreaseGameTickets(apdu);
                return true;
            
            case INS_USER_VIEW_GAMES:
                applet.readGames(apdu);
                return true;
            
            case INS_USER_FIND_GAME:
                applet.findGame(apdu);
                return true;
            
            // Không phải lệnh USER
            default:
                return false;
        }
    }
}
