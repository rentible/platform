package org.wallride.autoconfigure.twofactor;

import org.jboss.aerogear.security.otp.api.Base32;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class TOTPAuthenticator {
	public boolean verifyCode(String secret, int code, int variance)
			throws InvalidKeyException, NoSuchAlgorithmException, Base32.DecodingException {
		long timeIndex = System.currentTimeMillis() / 1000 / 30;
		byte[] secretBytes = Base32.decode(secret);
		for (int i = -variance; i <= variance; i++) {
			long calculatedCode = getCode(secretBytes, timeIndex + i);
			if (calculatedCode == code) {
				return true;
			}
		}
		return false;
	}

	public long getCode(byte[] secret, long timeIndex)
			throws NoSuchAlgorithmException, InvalidKeyException {
		SecretKeySpec signKey = new SecretKeySpec(secret, "HmacSHA1");
		//We put the timeIndex in a bytes array
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.putLong(timeIndex);
		byte[] timeBytes = buffer.array();

		//Calculate the SHA1
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(signKey);
		byte[] hash = mac.doFinal(timeBytes);

		//Calculate the offset we will use to extract our pin
		int offset = hash[19] & 0xf;
		//Clear the signed bits
		long truncatedHash = hash[offset] & 0x7f;
		//Use bits shift operations to copy the remaining 3 bytes from the array
		//and construct our number
		for (int i = 1; i < 4; i++) {
			truncatedHash <<= 8;
			truncatedHash |= hash[offset + i] & 0xff;
		}
		//Truncate to 6 digits
		return truncatedHash % 1000000;
	}
}
