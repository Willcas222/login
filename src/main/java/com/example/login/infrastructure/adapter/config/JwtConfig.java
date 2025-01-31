package com.example.login.infrastructure.adapter.config;

import com.example.login.aplication.service.JwtKeyProvider;
import com.nimbusds.jose.jwk.JWKSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;

@Configuration
public class JwtConfig {

    @Bean
    public JwtEncoder jwtEncoder() throws Exception {
        RSAPrivateKey privateKey = JwtKeyProvider.getPrivateKey();
        RSAPublicKey publicKey = JwtKeyProvider.getPublicKey();

        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        return new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get("private_key.pem")));

        Pattern pattern = Pattern.compile("-----BEGIN PRIVATE KEY-----(.*?)-----END PRIVATE KEY-----", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(key);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Clave privada en formato incorrecto");
        }

        byte[] decoded = Base64.getMimeDecoder().decode(matcher.group(1).replaceAll("\\s", ""));
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPrivateKey) keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private RSAPublicKey getPublicKey() throws Exception {
        String key = new String(Files.readAllBytes(Paths.get("public_key.pem")));

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


