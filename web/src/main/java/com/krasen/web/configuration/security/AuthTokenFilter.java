package com.krasen.web.configuration.security;

import com.krasen.web.utils.JwtUtils;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger( AuthTokenFilter.class );
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Autowired
    public AuthTokenFilter( JwtUtils jwtUtils, UserDetailsService userDetailsService ) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain ) throws ServletException, IOException {
        try {
            String token = parseJwt( request );
            if ( token != null && jwtUtils.validateToken( token ) ) {
                String username = jwtUtils.getUserNameFromToken( token );
                UserDetails userDetails = userDetailsService.loadUserByUsername( username );

                UsernamePasswordAuthenticationToken authentication;
                authentication = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities() );

                authentication.setDetails( new WebAuthenticationDetailsSource().buildDetails( request ) );
                SecurityContextHolder.getContext().setAuthentication( authentication );
            }
        } catch ( Exception e ) {
            logger.error( "Cannot set user authentication: {0}", e );
        }
        filterChain.doFilter( request, response );
    }

    private String parseJwt( HttpServletRequest request ) {
        String headerAuth = request.getHeader( "Authorization" );
        if ( StringUtils.hasText( headerAuth ) && headerAuth.startsWith( "Bearer " ) ) {
            return headerAuth.substring( 7 );
        }

        if ( isNull( request.getCookies() ) ) {
            return null;
        }

        Cookie authCookie = stream( request.getCookies() ).filter( c -> c.getName().equals( "auth_token" ) ).findFirst().orElse( null );
        if ( nonNull( authCookie ) ) {
            return authCookie.getValue();
        }

        return null;
    }

}