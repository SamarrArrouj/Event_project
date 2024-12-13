package com.example.Event_Project_Spring.service;


import com.example.Event_Project_Spring.entities.User;
import com.example.Event_Project_Spring.entities.VerificationToken;
import com.example.Event_Project_Spring.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.UUID;

@Service
public class VerificationTokenService {

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public VerificationToken createVerificationToken(User user) {
        String token;
        do {
            token = UUID.randomUUID().toString();  // Générer un nouveau token
        } while (tokenRepository.findByToken(token) != null);  // Vérifier s'il existe déjà

        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationToken.setExpiryDate(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000))); // Expire après 24 heures
        return tokenRepository.save(verificationToken);
    }


    public VerificationToken getVerificationToken(String token) {
        return tokenRepository.findByToken(token);
    }
}

