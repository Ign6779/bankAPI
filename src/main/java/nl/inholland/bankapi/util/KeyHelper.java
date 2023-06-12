package nl.inholland.bankapi.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class KeyHelper {

    private KeyHelper() {
        // private constructor for class with only static methods
    }

    public static Key getPrivateKey(String alias, String keystore, String password) throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableKeyException {
        Resource resource = new ClassPathResource(keystore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(resource.getInputStream(), password.toCharArray());
        return keyStore.getKey(alias, password.toCharArray());
    }
}