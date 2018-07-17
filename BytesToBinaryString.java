package util;

import java.math.BigInteger;

public class BytesToBinaryString {

	public static String binary(byte[] bytes, int radix) {
		return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数
	}
}
