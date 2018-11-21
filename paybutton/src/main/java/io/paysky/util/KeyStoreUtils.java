package io.paysky.util;

import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class KeyStoreUtils {

    private KeyStore keyStore;
    private String alias = "PAYSKY_BUTTON";
    private static KeyStoreUtils instance = null;


    public static KeyStoreUtils getInstance() {

        if (instance == null) {
            instance = new KeyStoreUtils();
        }


        return instance;

    }


    private KeyStoreUtils() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }


    public String decrypt(String cipherText) throws Exception {


        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
        Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

        CipherInputStream cipherInputStream = new CipherInputStream(
                new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
        ArrayList<Byte> values = new ArrayList<>();
        int nextByte;
        while ((nextByte = cipherInputStream.read()) != -1) {
            values.add((byte) nextByte);
        }

        byte[] bytes = new byte[values.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = values.get(i);
        }

        return new String(bytes, 0, bytes.length, "UTF-8");
    }

    public void deleteKey(final String alias) {
        try {
            keyStore.deleteEntry(alias);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

    }

    public String encrypt(String text) throws Exception {
        // Encrypt the text
        if (text.isEmpty()) {
            //  Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
            return "";
        }
        SecretKey key = (SecretKey) keyStore.getKey(alias, "PaySky".toCharArray());
        Cipher input = Cipher.getInstance("AES/ECB/PKCS5Padding", "AndroidOpenSSL");
        input.init(Cipher.ENCRYPT_MODE, key);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, input);
        cipherOutputStream.write(text.getBytes("UTF-8"));
        cipherOutputStream.close();
        byte[] vals = outputStream.toByteArray();
        return Base64.encodeToString(vals, Base64.DEFAULT);
    }

}
