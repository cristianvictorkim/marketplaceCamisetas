package com.uade.tpo.marketplace.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        String uri = request.getRequestURI();
        String message = "No tienes los permisos suficientes para acceder a este recurso";
        
        if (uri.startsWith("/api/carrito") || (uri.startsWith("/api/pedidos") && "POST".equalsIgnoreCase(request.getMethod()))) {
            message = "Debes ser un cliente registrado (y no un Administrador) para poder usar el Carrito o realizar pedidos.";
        }

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"status\":403,\"error\":\"FORBIDDEN\",\"message\":\"" + message + "\",\"path\":\""
                + uri + "\"}");
    }
}
