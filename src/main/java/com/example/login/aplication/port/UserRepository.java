package com.example.login.aplication.port;

import com.example.login.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByIdNumber(String idNumber);
    Optional<User>findById(Long userId);
    List<User> findAllByActiveTrue();
}
