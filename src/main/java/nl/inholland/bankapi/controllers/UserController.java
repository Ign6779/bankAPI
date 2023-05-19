package nl.inholland.bankapi.controllers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.UserTest;
import nl.inholland.bankapi.services.UserService;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    private UserService userService;
    public UserController (UserService userService){
        this.userService = userService;
    }

    // we will need Get Methods -Beth
    @GetMapping
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

    @GetMapping("/id/{id}")
    public ResponseEntity getUserById(@PathVariable int id){
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (EntityNotFoundException enfe) {
            return this.handleException(enfe);
        }

    }
    @GetMapping("/email/{email}")
    public ResponseEntity getUserByEmail(@PathVariable String email){
        try {
            return ResponseEntity.ok(userService.getUserByEmail(email));
        } catch (EntityNotFoundException enfe) {
            return this.handleException(enfe);
        }
    }
    @PostMapping
    public ResponseEntity createUser(@RequestBody UserTest userTest) {
        try {
            if(isUserFieldsValid(userTest)){
                userService.addUser(userTest);
                return ResponseEntity.status(201).body(null);
            }else
            {
                return ResponseEntity.status(400).body("Required fields are missing.");
            }

        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping("/{id}") // edit/update
    public ResponseEntity updateUser(@PathVariable Long id,@RequestBody UserTest userTest) {
        try {
            userService.updateUser(id,userTest);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }
    @PutMapping("/email/{email}") // edit/update
    public ResponseEntity updateUser(@PathVariable String  email,@RequestBody UserTest userTest) {
        try {
            userService.updateUser(email,userTest);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }
    @DeleteMapping("/{id}") // delete
    public ResponseEntity deleteUser(@PathVariable int id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @DeleteMapping("/email/{email}") // delete
    public ResponseEntity deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }

    private boolean isUserFieldsValid(UserTest userTest) {
        // Perform field validation here
        // For example, check if the required fields are not null or empty
        return userTest.getFirstName() != null && !userTest.getFirstName().isEmpty()
                && userTest.getEmail() != null && !userTest.getEmail().isEmpty()
                && userTest.getPhone() != null && !userTest.getPhone().isEmpty()
                && userTest.getRoles() != null && userTest.getDayLimit() >0
                && userTest.getTransactionLimit()>0;
    }
}
