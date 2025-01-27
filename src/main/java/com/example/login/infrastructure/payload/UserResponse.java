package com.example.login.infrastructure.payload;

import java.time.LocalDate;
import java.util.Date;


public class UserResponse {

    private String id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate birthDate;
    private String idNumber;
    private Date deletedDate;

    public UserResponse(Date deleetedDate,String id, String username, String password, String fullName, LocalDate birthDate, String idNumber) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDate = birthDate;
        this.idNumber = idNumber;
        this.deletedDate = deleetedDate;
    }

    public Date getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(Date deleetedDate) {
        this.deletedDate = deleetedDate;
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
