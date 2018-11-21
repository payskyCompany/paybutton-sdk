package io.paysky.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Ahmed AL Agamy on 8/27/2017.
 */

public class HashGenerator {
    //   DateTimeLocalTrxn=180829144425&MerchantId=11000000025&TerminalId=800022


    public static String encode(String key, String dateTime, String merchantId, String terminalId) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(Hex.decodeHex(key.toCharArray()), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            String enc = "DateTimeLocalTrxn=" + dateTime + "&MerchantId=" + merchantId + "&TerminalId=" + terminalId;
            return new String(Hex.encodeHex(sha256_HMAC.doFinal(enc.getBytes("UTF-8")))).toUpperCase();
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException | DecoderException e) {
            e.printStackTrace();
        }

        return null;
    }

}
