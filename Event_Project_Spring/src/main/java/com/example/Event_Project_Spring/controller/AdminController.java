package com.example.Event_Project_Spring.controller;

import com.example.Event_Project_Spring.entities.User;
import com.example.Event_Project_Spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("message", "Bienvenue, " + username);
        return "admin/admin-dashboard";
    }

    @GetMapping("/listUsers")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.read());
        return "admin/list-users";
    }

    @GetMapping("/addUserForm")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/add-user";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user) {
        userService.addUser(user);
        return "redirect:/admin/listUsers";
    }

    @GetMapping("/editUser/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        userService.updateUser(id, user);
        return "redirect:/admin/listUsers";
    }

    @GetMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/listUsers";
    }
}
