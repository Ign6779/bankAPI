package nl.inholland.bankapi.util;

import jakarta.annotation.PostConstruct;
import lombok.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

@Component
public class JwtKeyProvider {

    @Value("${jwt.key-store}")
    private String keystore;

    @Value("${jwt.key-store-password}")
    private String password;

    @Value("${jwt.key-alias}")
    private String alias;

    private Key privateKey;

    @PostConstruct
    protected void init() throws KeyStoreException, IOException, UnrecoverableKeyException, NoSuchAlgorithmException, CertificateException {
        Resource resource = new ClassPathResource(keystore);
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(resource.getInputStream(), password.toCharArray());
        privateKey = keyStore.getKey(alias, password.toCharArray());
    }

    public Key getPrivateKey() {
        return privateKey;
    }
}
