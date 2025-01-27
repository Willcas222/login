package com.example.login.aplication.service;


import com.example.login.aplication.port.output.UserRepository;
import com.example.login.domain.model.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<User> getAllActiveUsers() {
        return userRepository.findAllByActiveTrue();
    }

    public void desactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));


        if (!user.getActive()) {
            throw new RuntimeException("El usuario ya está desactivado");
        }

        user.setActive(false);
        user.setDeletedDate(new Date());
        userRepository.save(user);
    }
}
