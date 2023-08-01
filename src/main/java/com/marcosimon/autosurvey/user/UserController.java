package com.marcosimon.autosurvey.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")

public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome, this endpoint is not secure");
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UserModel>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping(path = "{name}")
    public ResponseEntity<UserModel> getUser(@PathVariable String name) {

        UserModel userModel = userService.getUserByName(name);
        if (userModel == null) return ResponseEntity.notFound().build();

        return  ResponseEntity.ok(userModel);
    }

    @PostMapping("/new")
    public ResponseEntity<String> createUser(@RequestBody UserModel userModel) {
        String result = userService.createUser(userModel);
        if (result == null) return  ResponseEntity.status(409).build();

        return ResponseEntity.status(201).build();
    }
}
