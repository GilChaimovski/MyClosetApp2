package com.example.mycloset.models;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecurityHelper {
    public static String secret = "d1mat6ni5th3b35t";
    public static SecretKey key = new SecretKeySpec(secret.getBytes(), "AES");

    public static String Encrypt(String text) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = cipher.doFinal(text.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String Decrypt(String text) {
        try {
            byte[] decodedText = Base64.getDecoder().decode(text);
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            String decryptString = new String(cipher.doFinal(decodedText), "UTF-8");
            return decryptString;
        }
        catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
