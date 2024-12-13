package com.example.Event_Project_Spring.service;
import com.example.Event_Project_Spring.ENUM.Role;
import com.example.Event_Project_Spring.entities.User;
import com.example.Event_Project_Spring.repository.UserRepository;
import com.example.Event_Project_Spring.security.jwt.JwtUtils;
import com.example.Event_Project_Spring.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRep;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User addUser(User user) {
        if (userRep.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Error: Email is already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmailVerified(false);
        user.setRole(Role.participant);
        return userRep.save(user);
    }


    @Override
    public List<User> read() {

        return userRep.findAll();
    }

    @Override
    public User updateUser(Long id, User user) {
        return userRep.findById(id)
                .map(existingUser -> {
                    existingUser.setUsername(user.getUsername());
                    existingUser.setEmail(user.getEmail());

                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        String hashedPassword = passwordEncoder.encode(user.getPassword());
                        existingUser.setPassword(hashedPassword);
                    }

                    existingUser.setRole(user.getRole());
                    return userRep.save(existingUser);
                })
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé !"));
    }


    @Override
    public String deleteUser(Long id) {

        UserDetailsImpl currentUser = JwtUtils.getCurrentUser();

        if (currentUser == null) {
            throw new RuntimeException("Utilisateur non authentifié !");
        }

        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            throw new RuntimeException("Action non autorisée. Seuls les administrateurs peuvent supprimer des utilisateurs.");
        }
        userRep.deleteById(id);
        return "User deleted successfully !";
    }
    @Override
    public User findByUsername(String username) {
        return userRep.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec le nom d'utilisateur : " + username));
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + id));
    }
}
