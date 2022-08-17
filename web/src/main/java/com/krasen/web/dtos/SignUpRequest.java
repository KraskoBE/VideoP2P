package com.krasen.web.dtos;

import javax.validation.constraints.*;

import lombok.*;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Size( min = 6, max = 30 )
    private String password;

    @NotBlank
    private String email;

}
