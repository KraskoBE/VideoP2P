package com.krasen.web.services.interfaces;

import com.krasen.web.dtos.LoginRequest;
import com.krasen.web.dtos.LoginResponse;
import com.krasen.web.dtos.SignUpRequest;
import com.krasen.web.dtos.SignUpResponse;

public interface AuthService {

    SignUpResponse register( SignUpRequest user ) throws Exception;

    LoginResponse login( LoginRequest loginRequest);

}