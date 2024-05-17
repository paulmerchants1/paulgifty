package com.auth.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

public class SecureHelper {
    public  String decryptKey(String key, String strToDecrypt) {
        Security.addProvider(new BouncyCastleProvider());
        byte[] keyBytes;

        try {
            keyBytes = key.getBytes("UTF-8");
            SecretKeySpec skey = new SecretKeySpec(keyBytes, "AES");
            byte[] input = org.bouncycastle.util.encoders.Base64.decode(strToDecrypt.trim().getBytes("UTF-8"));

            synchronized (Cipher.class) {
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
                cipher.init(Cipher.DECRYPT_MODE, skey);

                byte[] plainText = new byte[cipher.getOutputSize(input.length)];
                int ptLength = cipher.update(input, 0, input.length, plainText, 0);
                ptLength += cipher.doFinal(plainText, ptLength);
                String decryptedString = new String(plainText).trim();
                return decryptedString;
            }
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
        } catch (IllegalBlockSizeException ibse) {
            ibse.printStackTrace();
        } catch (BadPaddingException bpe) {
            bpe.printStackTrace();
        } catch (InvalidKeyException ike) {
            ike.printStackTrace();
        } catch (NoSuchPaddingException nspe) {
            nspe.printStackTrace();
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        } catch (ShortBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}

