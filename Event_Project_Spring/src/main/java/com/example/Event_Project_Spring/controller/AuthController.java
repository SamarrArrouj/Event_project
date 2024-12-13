package com.example.Event_Project_Spring.controller;


import com.example.Event_Project_Spring.ENUM.Role;
import com.example.Event_Project_Spring.entities.User;
import com.example.Event_Project_Spring.entities.VerificationToken;
import com.example.Event_Project_Spring.payload.request.LoginRequest;
import com.example.Event_Project_Spring.payload.request.SignupRequest;
import com.example.Event_Project_Spring.payload.response.JwtResponse;
import com.example.Event_Project_Spring.payload.response.MessageResponse;
import com.example.Event_Project_Spring.repository.UserRepository;
import com.example.Event_Project_Spring.security.jwt.JwtUtils;
import com.example.Event_Project_Spring.security.services.UserDetailsImpl;
import com.example.Event_Project_Spring.service.EmailService;
import com.example.Event_Project_Spring.service.VerificationTokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/user")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    private VerificationTokenService tokenService;

    @Autowired
    private EmailService emailService;

    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Log pour vérifier le statut de l'email
        System.out.println("Username: " + userDetails.getUsername());
        System.out.println("Email Verified: " + user.getEmailVerified());

        if (!user.getEmailVerified()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Error: Email is not verified!"));
        }

        String jwt = jwtUtils.generateJwtToken(authentication);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles, jwt));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        // Initialisation du rôle de l'utilisateur
        Role role = Role.participant;

        List<String> strRoles = signUpRequest.getRoles();
        if (strRoles != null && strRoles.contains("admin")) {
            role = Role.admin; // Assigner le rôle "admin" si spécifié
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                role,
                false
        );

        userRepository.save(user);

        VerificationToken token = tokenService.createVerificationToken(user);
        emailService.sendVerificationEmail(user, token.getToken());

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Please check your email to verify."));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        VerificationToken verificationToken = tokenService.getVerificationToken(token);
        if (verificationToken == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification token!"));
        }

        User user = verificationToken.getUser();
        if (user.isEmailVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Email is already verified!"));
        }

        user.setEmailVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Email verified successfully!"));
    }


}

