package nl.inholland.bankapi.controllers;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.java.Log;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.services.UserService;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("users")
@Log
public class UserController {
    private UserService userService;
    public UserController (UserService userService){
        this.userService = userService;
    }

    // we will need Get Methods -Beth
    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getAllUsers(@RequestParam(required = false) Integer offset,
    @RequestParam(required = false) Integer limit,
    @RequestParam(required = false) Boolean hasAccount){
        try {
            return ResponseEntity.ok(userService.getAllUsers( offset,  limit,  hasAccount));
        }
        catch (Exception e){
            return  this.handleException(e);
        }
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity getUserById(@PathVariable UUID id){
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException enfe) {
            return this.handleException(enfe);
        }

    }

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity createUser(@RequestBody User user) {
        try {
            if(isUserFieldsValid(user)){
                return ResponseEntity.status(201).body(userService.addUser(user));
            }else
            {
                return ResponseEntity.status(400).body("Required fields are missing.");
            }
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping("/{id}") // edit/update
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity updateUser(@PathVariable UUID id,@RequestBody User user) {
        try {
            return ResponseEntity.status(200).body(userService.updateUser(id, user));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @DeleteMapping("/{id}") // delete
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity deleteUser(@PathVariable UUID id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }

    private boolean isUserFieldsValid(User user) {
        // Perform field validation here
        // For example, check if the required fields are not null or empty
        return user.getFirstName() != null && !user.getFirstName().isEmpty()
                && user.getEmail() != null && !user.getEmail().isEmpty()
                && user.getPhone() != null && !user.getPhone().isEmpty()
                && user.getRoles() != null && user.getDayLimit() >0
                && user.getTransactionLimit()>0;
    }
}
