package kr.hvy.blog.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64WebSafeUtil {

    private static final Base64.Encoder bEncode = java.util.Base64.getEncoder();

    /**
     * String to WebSafe Base64 String
     * @param message
     * @return byte[]
     */
    public static String encode(String message) throws UnsupportedEncodingException {
        return encode(message.getBytes("UTF-8"));
    }

    /**
     * Byte array to WebSafe Base64 String
     * @param message
     * @return String
     */
    public static String encode(byte[] message) {
        // https://gist.github.com/geraintluff/21beb1066fc5239304aa
        String enc = bEncode.encodeToString(message);
        return StringUtils.stripEnd(enc, "=").replaceAll("\\+", "-").replaceAll("/", "_");
    }

    /**
     * WebSafe Base64 String to byte array
     * @param message
     * @return byte[]
     */
    public static byte[] decodeToBytes(String message) {
        return Base64.getDecoder().decode(decodeToNormal(message));
    }

    /**
     * WebSafe Base64 String to normal Base64 String
     * @param message
     * @return String
     */
    public static String decodeToNormal(String message) {
        return message.replaceAll("\\-", "+").replaceAll("_", "/") + "==".substring(0, (message.length() * 3) % 4);
    }



}
