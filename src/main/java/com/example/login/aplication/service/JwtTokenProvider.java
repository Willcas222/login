package com.example.login.aplication.service;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.List;

@Component
public class JwtTokenProvider {

    private final JwtEncoder jwtEncoder;

    public JwtTokenProvider() throws Exception {
        // Obtener las claves
        RSAPrivateKey privateKey = JwtKeyProvider.getPrivateKey();
        RSAPublicKey publicKey = JwtKeyProvider.getPublicKey();

        // Crear RSAKey con las claves pública y privada
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .build();

        // Usar NimbusJwtEncoder con la clave RSA
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableJWKSet<>(new JWKSet(rsaKey)));
    }

    public String createToken(String username, List<String> roles) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(username)
                .claim("roles", roles)
                .claim("scope", "ROLE_ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600)) // 1 hora de expiración
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}
