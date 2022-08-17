package com.krasen.web.configuration;

import javax.servlet.http.*;

import org.springframework.security.web.RedirectStrategy;

public class NoRedirectStrategy implements RedirectStrategy {

    @Override
    public void sendRedirect( HttpServletRequest request, HttpServletResponse response, String url ) {
    }

}
