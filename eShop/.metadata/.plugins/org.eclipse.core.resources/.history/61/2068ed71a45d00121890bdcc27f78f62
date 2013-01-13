/*
 * MD5 hash calculator wrapper
 * ver.-1.00
 * (C) 12.01.2013 zhgzhg
 */

package md5_calculator;

import java.security.MessageDigest;
import javax.crypto.*;

public class Md5hashcalc {
	
	public static String calculateMD5hash(String word) throws Exception {
		
		MessageDigest md5;
		String result = "D41D8CD98F00B204E9800998ECF8427E"; //empty string
		
		md5 = MessageDigest.getInstance("MD5");
		md5.update(word.getBytes(), 0, word.getBytes().length);
		
		byte [] wordBytesHash = new byte[16];
		wordBytesHash = md5.digest();
		
		if (wordBytesHash.length != 0) {
			result = "";
		
			for (int i = 0; i < 16; i++) {
				
				result += Integer.toString((wordBytesHash[i] & 0xff) + 0x100, 16).substring(1);
			}
		}
		
		return result;
	}
}
