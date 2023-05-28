package nl.inholland.bankapi.controllers;

import jakarta.annotation.security.PermitAll;
import nl.inholland.bankapi.models.dto.*;
import nl.inholland.bankapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/login")
    public Object login(@RequestBody LoginDTO dto) throws Exception {
        try {
            return new TokenDTO(
                    userService.login(dto.username(), dto.password())
            );
        } catch (Exception e){
            return  this.handleException(e);
        }

    }

    @PostMapping("/register")
    public Object register(@RequestBody RegisterDTO dto) throws Exception{
        try {
            return userService.register(dto);
        } catch (Exception e){
            return  this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }

}
