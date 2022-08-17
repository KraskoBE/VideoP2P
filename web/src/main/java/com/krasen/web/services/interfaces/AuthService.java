package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.*;

public interface AuthService {

    SignUpResponse register( SignUpRequest user ) throws Exception;

    LoginResponse login( LoginRequest loginRequest );

}