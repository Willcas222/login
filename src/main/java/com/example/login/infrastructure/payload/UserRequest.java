package com.example.login.infrastructure.payload;

import com.example.login.domain.model.Role;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


public class UserRequest {

    private String id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String idNumber;
    private Role role;

    public UserRequest(String id, String username, String password, String fullName, LocalDate birthDate, String idNumber,Role role ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.idNumber = idNumber;
        this.role = role;

    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }
}
