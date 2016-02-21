package com.maoshen.util.encry;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DESUtil {
    private static final String Algorithm = "DESede"; //定义 加密算法,可用 DES,DESede,Blowfish  

    //keybyte为加密密钥，长度为24字节    
    //src为被加密的数据缓冲区（源）    
    public static byte[] encrypt(String keyString,byte[] src) {
        try {
            byte[] keybyte=keyString.getBytes();
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //加密            
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace();
        }
        return null;
    }
    //keybyte为加密密钥，长度为24字节
    //src为加密后的缓冲区
    public static byte[] decrypt(String keyString,byte[] src) {
        try {
            byte[] keybyte=keyString.getBytes();
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            return c1.doFinal(src);
        } catch (java.security.NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        } catch (javax.crypto.NoSuchPaddingException e2) {
            e2.printStackTrace();
        } catch (java.lang.Exception e3) {
            e3.printStackTrace(); 
        }
        return null;
    }      
}
