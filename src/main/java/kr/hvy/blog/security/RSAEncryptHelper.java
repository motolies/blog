package kr.hvy.blog.security;

import kr.hvy.blog.entity.redis.RsaHash;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class RSAEncryptHelper {

    private static final String algorithm = "RSA";

    public static RsaHash makeRsaHash() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        return RsaHash.builder()
                .privateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()))
                .publicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()))
                .build();

    }

    public static Map<String, Object> makeKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        String tmpPublicKey = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()); // 공개키
        String tmpPrivateKey = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()); // 개인키

        Map<String, Object> pair = new HashMap<String, Object>();
        pair.put("privateKeyString", tmpPrivateKey);
        pair.put("publicKeyString", tmpPublicKey);
        pair.put("privateKey", keyPair.getPrivate().getEncoded());
        pair.put("publicKey", keyPair.getPublic().getEncoded());
        return pair;
    }

    public static byte[] getKeyBytes(String key) {
        return Base64.getDecoder().decode(key);
    }

    public static PrivateKey getPrivateKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        PrivateKey privKey = KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        return privKey;
    }

    public static PublicKey getPublicKey(String key) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] decodedKey = Base64.getDecoder().decode(key);
        PublicKey publKey = KeyFactory.getInstance(algorithm).generatePublic(new PKCS8EncodedKeySpec(decodedKey));
        return publKey;
    }

    public static String getDecryptMessage(String secretMsg, byte[] privateKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException, InvalidAlgorithmParameterException {

        PrivateKey privKey = KeyFactory.getInstance(algorithm).generatePrivate(new PKCS8EncodedKeySpec(privateKey));

        // RSA/ECB/OAEPWithSHA-256AndMGF1Padding
        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privKey);

        // RSA/ECB/OAEPPadding
//		Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");
//		OAEPParameterSpec oaepParams = new OAEPParameterSpec("SHA-256", "MGF1", new MGF1ParameterSpec("SHA-256"), PSource.PSpecified.DEFAULT);
//		cipher.init(Cipher.DECRYPT_MODE, privKey, oaepParams);

        byte[] bMsg = Base64.getDecoder().decode(secretMsg);
        byte[] decryptMsg = cipher.doFinal(bMsg);
        return new String(decryptMsg);
    }

    public static String getDecryptMessage(String secretMsg, String privateKey) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
        Cipher cipher = Cipher.getInstance(algorithm);
        PrivateKey pk = getPrivateKey(privateKey);
        cipher.init(Cipher.DECRYPT_MODE, pk);
        byte[] bMsg = Base64.getDecoder().decode(secretMsg);
        byte[] decryptMsg = cipher.doFinal(bMsg);
        return new String(decryptMsg);
    }

}