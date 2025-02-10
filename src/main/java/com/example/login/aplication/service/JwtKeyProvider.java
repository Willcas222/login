package com.example.login.aplication.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class JwtKeyProvider {

    @Value("${app.private-key}")
    private String privateKeyPEM;

    @Value("${app.public-key}")
    private String publicKeyPEM;

    public RSAPrivateKey getPrivateKey() throws Exception {
        return parsePrivateKey(privateKeyPEM);
    }

    public RSAPublicKey getPublicKey() throws Exception {
        return parsePublicKey(publicKeyPEM);
    }

    private RSAPrivateKey parsePrivateKey(String key) throws Exception {
        Pattern pattern = Pattern.compile("-----BEGIN PRIVATE KEY-----(.*?)-----END PRIVATE KEY-----", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(key);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Clave privada en formato incorrecto");
        }
        byte[] decoded = Base64.getMimeDecoder().decode(matcher.group(1).replaceAll("\\s", ""));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private RSAPublicKey parsePublicKey(String key) throws Exception {
        Pattern pattern = Pattern.compile("-----BEGIN PUBLIC KEY-----(.*?)-----END PUBLIC KEY-----", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(key);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Clave pública en formato incorrecto");
        }
        byte[] decoded = Base64.getMimeDecoder().decode(matcher.group(1).replaceAll("\\s", ""));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decoded));
    }
}


