package co.com.crediya.security.util;

import co.com.crediya.exceptions.CrediyaInternalServerErrorException;
import co.com.crediya.security.enums.SecurityConstants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class KeysUtilTest {

    private KeysUtil keysUtil;

    private String generateValidPublicKeyPEM() throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) pair.getPublic();

        String base64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());
        return "-----BEGIN PUBLIC KEY-----\n" + base64 + "\n-----END PUBLIC KEY-----";
    }

    @Test
    @DisplayName("Must load public key successfully")
    void testLoadPublicKeySuccess() throws Exception {
        String pem = generateValidPublicKeyPEM();
        keysUtil = new KeysUtil(pem);

        RSAPublicKey result = keysUtil.loadPublicKey();

        assertNotNull(result);
        assertEquals("RSA", result.getAlgorithm());
        assertEquals(SecurityConstants.TYPE_ALGORITHM.getValue(), result.getAlgorithm());
    }

    @Test
    @DisplayName("Must throw exception when public key is invalid")
    void testLoadPublicKeyFailure() {
        String invalidKey = "-----BEGIN PUBLIC KEY-----\nabc123!@#\n-----END PUBLIC KEY-----";
        keysUtil = new KeysUtil(invalidKey);

        assertThrows(CrediyaInternalServerErrorException.class, () -> keysUtil.loadPublicKey());
    }
}
