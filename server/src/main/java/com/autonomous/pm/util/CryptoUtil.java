package com.autonomous.pm.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;

import com.autonomous.pm.config.AppProperties;



/**
 */
//@Slf4j
public class CryptoUtil {

	/**
	 * 
	 * @param key
	 * @throws UnsupportedEncodingException
	 */
	private static String _AES256_KEY_ = AppProperties.instance().getProperty("aes256.encrypt.key");	// 17글자

	public static String encryptAES256(String msg) {
		SecretKeyFactory factory;
		byte[] buffer = null;
		
		try {
		
		    SecureRandom random = new SecureRandom();
		    byte bytes[] = new byte[20];
		    random.nextBytes(bytes);
		    byte[] saltBytes = bytes;

			factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(_AES256_KEY_.toCharArray(), saltBytes, 70000, 256);
			
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secret);
			AlgorithmParameters params = cipher.getParameters();
			
			if (params != null && params.getParameterSpec(IvParameterSpec.class) != null ) {
				byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();
				byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));
				buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];
				System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);
				System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);
				System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);
			}
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
		} catch (InvalidKeySpecException e) {
			System.out.println(e.toString());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.toString());
		} catch (InvalidKeyException e) {
			System.out.println(e.toString());
		} catch (InvalidParameterSpecException e) {
			System.out.println(e.toString());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.toString());
		} catch (BadPaddingException e) {
			System.out.println(e.toString());
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString());
		}

		if ( buffer != null ) {
			return Base64.getEncoder().encodeToString(buffer);
		} else {
			return null;
		}
		
	}

	public static String decryptAES256(String msg) {
	    Cipher cipher = null;
	    byte[] decryptedTextBytes = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));
			
			byte[] saltBytes = new byte[20];
			buffer.get(saltBytes, 0, saltBytes.length);
			byte[] ivBytes = new byte[cipher.getBlockSize()];
			buffer.get(ivBytes, 0, ivBytes.length);
			byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];
			buffer.get(encryoptedTextBytes);
			
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
			PBEKeySpec spec = new PBEKeySpec(_AES256_KEY_.toCharArray(), saltBytes, 70000, 256);
			
			SecretKey secretKey = factory.generateSecret(spec);
			SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");
			
			cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));
			
			decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);
			
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
		} catch (NoSuchPaddingException e) {
			System.out.println(e.toString());
		} catch (InvalidKeySpecException e) {
			System.out.println(e.toString());
		} catch (InvalidKeyException e) {
			System.out.println(e.toString());
		} catch (InvalidAlgorithmParameterException e) {
			System.out.println(e.toString());
		} catch (IllegalBlockSizeException e) {
			System.out.println(e.toString());
		} catch (BadPaddingException e) {
			System.out.println(e.toString());
		}
	    return new String(decryptedTextBytes);
	}
	
}