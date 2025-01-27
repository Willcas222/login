package com.example.login.aplication.service;

import com.example.login.aplication.port.output.UserRepository;
import com.example.login.domain.model.User;

import com.example.login.infrastructure.adapter.config.ConfigPaswoord;
import com.example.login.infrastructure.payload.UserRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;

    public AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public Optional<User> login(String username, String plainPassword) {
        return userRepository.findByUsername(username)
                .filter(user -> {
                    if (!user.getActive()) {
                        return false;
                    }
                    byte[] salt = ConfigPaswoord.decodeSalt(user.getSalt());
                    String hashedPassword = ConfigPaswoord.hashPassword(plainPassword, salt);
                    return hashedPassword.equals(user.getPassword());
                });
    }


    public User register(String email, String plainPassword, String fullName, LocalDate birthDate, String idNumber) {

        if (!isValidEmail(email)){
            throw new IllegalArgumentException("El formato del correo es invalido");
        }

        if(userRepository.findByUsername(email).isPresent()){
            throw new IllegalArgumentException("Ya existe un usuario con ese email");
        }
        if (userRepository.findByIdNumber(idNumber).isPresent()) {
            throw new IllegalArgumentException("El número de cédula ya está registrado.");
        }

        byte[] salt = ConfigPaswoord.generateSalt();
        String encodeSalt = ConfigPaswoord.encodeSalt(salt);
        String hashedPassword = ConfigPaswoord.hashPassword(plainPassword, salt);

        User user = new User();
        user.setUsername(email);
        user.setPassword(hashedPassword);
        user.setSalt(encodeSalt);
        user.setFullName(fullName);
        user.setBirthDate(birthDate);
        user.setIdNumber(idNumber);
        user.setActive(true);


        return userRepository.save(user);
    }



    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public User updateUser(Long id, UserRequest userRequest) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        if (userRequest.getFullName() != null) {
            user.setFullName(userRequest.getFullName());
        }
        if (userRequest.getBirthDate() != null) {
            user.setBirthDate(userRequest.getBirthDate());
        }
        if (userRequest.getPassword() != null) {
            byte[] salt = ConfigPaswoord.generateSalt();
            String encodedSalt = ConfigPaswoord.encodeSalt(salt);
            String hashedPassword = ConfigPaswoord.hashPassword(userRequest.getPassword(), salt);
            user.setPassword(hashedPassword);
            user.setSalt(encodedSalt);
        }

        return userRepository.save(user);
    }

}


