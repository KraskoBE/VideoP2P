package com.krasen.web.configuration.security;

import com.krasen.web.models.User;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger( JwtUtils.class );

    @Value( "${videop2p.app.jwtSecret}" )
    private String jwtSecret;

    @Value( "${videop2p.app.jwtExpirationMs}" )
    private int jwtExpirationMs;


    public String generateToken( Authentication authentication ) {
        User userPrincipal = (User) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject( ( userPrincipal.getUsername() ) )
                .setIssuedAt( new Date() )
                .setExpiration( new Date( ( new Date() ).getTime() + jwtExpirationMs ) )
                .signWith( Keys.hmacShaKeyFor( Decoders.BASE64.decode( jwtSecret ) ) )
                .compact();

    }

    public String getUserNameFromToken( String token ) {
        return Jwts.parserBuilder()
                .setSigningKey( Keys.hmacShaKeyFor( Decoders.BASE64.decode( jwtSecret ) ) )
                .build()
                .parseClaimsJws( token )
                .getBody()
                .getSubject();
    }

    public boolean validateToken( String token ) {
        try {
            Jwts.parserBuilder().setSigningKey( Keys.hmacShaKeyFor( Decoders.BASE64.decode( jwtSecret ) ) ).build().parseClaimsJws( token );
            return true;
        } catch ( SignatureException e ) {
            logger.error( "Invalid JWT signature: {}", e.getMessage() );
        } catch ( MalformedJwtException e ) {
            logger.error( "Invalid JWT token: {}", e.getMessage() );
        } catch ( ExpiredJwtException e ) {
            logger.error( "JWT token is expired: {}", e.getMessage() );
        } catch ( UnsupportedJwtException e ) {
            logger.error( "JWT token is unsupported: {}", e.getMessage() );
        } catch ( IllegalArgumentException e ) {
            logger.error( "JWT claims string is empty: {}", e.getMessage() );
        }
        return false;
    }

}
