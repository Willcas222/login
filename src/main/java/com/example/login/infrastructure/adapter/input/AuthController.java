package com.example.login.infrastructure.adapter.input;


import com.example.login.aplication.service.AuthenticationService;
import com.example.login.aplication.service.UserService;
import com.example.login.domain.model.User;
import com.example.login.infrastructure.payload.UserRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    public AuthController(AuthenticationService authenticationService,UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveUser() {
        List<User> activeUsers = userService.getAllActiveUsers();

        if (activeUsers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No hay usuarios activos"));
        }

        User user = activeUsers.get(0);
        return ResponseEntity.ok(Map.of(
                "id", user.getId(),
                "fullName", user.getFullName(),
                "username", user.getUsername()
        ));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.desactivateUser(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Usuario no encontrado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("usuario ya se encuentra desactivado");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest request) {
        String email = request.getUsername();
        String password = request.getPassword();

        return authenticationService.login(email, password)
                .map(user -> ResponseEntity.ok(Map.of("message", "Login exitoso")))
                .orElse(ResponseEntity.status(401).body(Map.of("error","Credenciales invalidas")));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest request) {
        try {
            String email = request.getUsername();
            String password = request.getPassword();
            String fullName = request.getFullName();
            LocalDate birthDate = request.getBirthDate();
            String idNumber = request.getIdNumber();



            User user = authenticationService.register(email, password, fullName, birthDate, idNumber);

            return ResponseEntity.ok(Map.of("message", "Registro exitoso", "id", user.getId()));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().body(Map.of("error",e.getMessage()));
        }catch (Exception e){
            return ResponseEntity.status(500).body(Map.of("error", "error en el registro"));
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        try {
            User updatedUser = authenticationService.updateUser(id, userRequest);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario actualizado exitosamente",
                    "fullName", updatedUser.getFullName(),
                    "username", updatedUser.getUsername()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error al actualizar el usuario"));
        }
    }



}
