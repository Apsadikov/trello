package ru.itis.trello.security.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class BasicAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletRequest.setAttribute("email", httpServletRequest.getParameter("email"));
        httpServletRequest.setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, e);
        httpServletRequest.getRequestDispatcher("/sign-in-error").forward(httpServletRequest, httpServletResponse);
    }
}
