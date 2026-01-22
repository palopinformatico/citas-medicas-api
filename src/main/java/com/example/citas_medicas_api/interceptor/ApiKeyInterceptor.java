package com.example.citas_medicas_api.interceptor;

import com.example.citas_medicas_api.exception.BusinessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${spring.security.api-key.paciente}")
    private String pacienteApiKey;

    @Value("${spring.security.api-key.medico}")
    private String medicoApiKey;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) 
            throws Exception {
        
        String apiKey = request.getHeader("X-API-Key");
        
        if (apiKey == null || apiKey.isEmpty()) {
            throw new BusinessException("API Key no proporcionada");
        }
        
        if (!isValidApiKey(apiKey)) {
            throw new BusinessException("API Key inválida");
        }
        
        // Guardar el tipo de usuario en el request
        if (apiKey.equals(pacienteApiKey)) {
            request.setAttribute("userRole", "PATIENT");
        } else if (apiKey.equals(medicoApiKey)) {
            request.setAttribute("userRole", "DOCTOR");
        }
        
        return true;
    }

    private boolean isValidApiKey(String apiKey) {
        return apiKey.equals(pacienteApiKey) || apiKey.equals(medicoApiKey);
    }
}
