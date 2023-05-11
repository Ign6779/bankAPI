package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.UserTest;
import nl.inholland.bankapi.services.UserService;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;
    public UserController (UserService userService){
        this.userService = userService;
    }

    // we will need Get Methods -Beth
    @GetMapping
    public ResponseEntity getAllUsers(){
        try {
            return ResponseEntity.ok( userService.getAllUsers());
        }
        catch (Exception e){
            return  this.handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserTest userTest) {
        try {
            userService.addUser(userTest);
            return ResponseEntity.status(201).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping // edit/update
    public ResponseEntity updateUser(@RequestBody UserTest userTest) {
        try {
            userService.updateUser(userTest);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @DeleteMapping // delete
    public ResponseEntity deleteUser(@RequestBody UserTest userTest) {
        try {
            userService.deleteUser(userTest.getUuid());
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }
}
