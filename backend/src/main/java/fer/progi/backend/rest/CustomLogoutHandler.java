package fer.progi.backend.rest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;

public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (request.getSession(false) != null) {
            request.getSession().invalidate();
        }

        SecurityContextHolder.clearContext();
        System.out.println("User logged out and SecurityContext cleared");
    }
}
