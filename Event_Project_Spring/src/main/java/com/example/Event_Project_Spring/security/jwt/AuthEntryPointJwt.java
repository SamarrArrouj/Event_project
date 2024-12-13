package com.example.Event_Project_Spring.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;

//authenticationEntryPoint, qui spécifie la technique d’authentification
//AuthenticationEntryPoint is to allow the framework to send some sort of
//"to access this resource you must authenticate first" notification from application server to web client
//BasicAuthenticationEntryPoint : par defaut elle interdit l'accès à toute les ressources
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        logger.error("Unauthorized error: {}", authException.getMessage());

        String errorMessage = "Error: Unauthorized - " + authException.getMessage();
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }

}