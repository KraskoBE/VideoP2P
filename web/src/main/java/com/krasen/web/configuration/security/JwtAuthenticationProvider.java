package com.krasen.web.configuration.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    //    @Autowired
    //    private JwtUtil jwtUtil;

    @Override
    public boolean supports( Class<?> authentication ) {
        //                return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
        return true;
    }

    @Override
    protected void additionalAuthenticationChecks( UserDetails userDetails, UsernamePasswordAuthenticationToken authentication ) throws
            AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser( String username, UsernamePasswordAuthenticationToken authentication ) throws
            AuthenticationException {
        //        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        //        String token = jwtAuthenticationToken.getToken();
        //
        //        User parsedUser = jwtUtil.parseToken(token);
        //
        //        if (parsedUser == null) {
        //            throw new JwtTokenMalformedException("JWT token is not valid");
        //        }
        //
        //        List<GrantedAuthority> authorityList = AuthorityUtils.commaSeparatedStringToAuthorityList(parsedUser.getRole());
        //
        //        return new AuthenticatedUser(parsedUser.getId(), parsedUser.getUsername(), token, authorityList);
        return null;
    }

}
