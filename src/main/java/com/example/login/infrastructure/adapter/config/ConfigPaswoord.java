package com.example.login.infrastructure.adapter.config;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/*PBKDF2 (Password-Based Key Derivation Function 2)*/

public class ConfigPaswoord {


        private static final int SALT_LENGTH = 16;
        private static final int HASH_LENGTH = 256;
        private static final int ITERATIONS = 10000;


        public static String hashPassword(String plainPassword, byte[] salt) {
            try {

                PBEKeySpec spec = new PBEKeySpec(plainPassword.toCharArray(), salt, ITERATIONS, HASH_LENGTH);
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                byte[] hashedBytes = keyFactory.generateSecret(spec).getEncoded();


                return Base64.getEncoder().encodeToString(hashedBytes);
            } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                throw new RuntimeException("Error al generar el hash de la contraseña", e);
            }
        }


        public static byte[] generateSalt() {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            return salt;
        }


        public static String encodeSalt(byte[] salt) {
            return Base64.getEncoder().encodeToString(salt);
        }


        public static byte[] decodeSalt(String encodedSalt) {
            return Base64.getDecoder().decode(encodedSalt);
        }

}
