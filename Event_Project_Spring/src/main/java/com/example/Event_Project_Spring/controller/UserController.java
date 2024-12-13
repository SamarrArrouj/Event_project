package com.example.Event_Project_Spring.controller;

import com.example.Event_Project_Spring.entities.User;
import com.example.Event_Project_Spring.security.jwt.JwtUtils;
import com.example.Event_Project_Spring.security.services.UserDetailsImpl;
import com.example.Event_Project_Spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userServ;
    @PostMapping("/addUser")
    public User addUser(@RequestBody User user) {
        UserDetailsImpl currentUser = JwtUtils.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        if (!currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Action non autorisée. Seuls les administrateurs peuvent ajouter des utilisateurs.");
        }

        return userServ.addUser(user);
    }

    @GetMapping("/listUsers")
    List<User> listUsers() {
        return userServ.read();

    }
    @PutMapping("/update/{id}")
    public User update(@PathVariable Long id,@RequestBody User user) {
        UserDetailsImpl currentUser = JwtUtils.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non authentifié");
        }

        if (!currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Action non autorisée. Seuls les administrateurs peuvent mettre à jour des utilisateurs.");
        }

        return userServ.updateUser(id,user);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        UserDetailsImpl currentUser = JwtUtils.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non authentifié !");
        }

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Action non autorisée. Seuls les administrateurs peuvent supprimer des utilisateurs.");
        }

        return userServ.deleteUser(id);
    }

}

