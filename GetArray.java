package util;

public class GetArray {
	public static byte[] getBooleanArray(byte b[]) {  
        byte[] array = new byte[16];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte)(b[1] & 1);  
            b[1] = (byte) (b[1] >> 1);  
        }
        for (int i = 15; i >= 8; i--) {
            array[i] = (byte)(b[0] & 1);  
            b[0] = (byte) (b[0] >> 1);  
        }
        return array;  
    }  
}
