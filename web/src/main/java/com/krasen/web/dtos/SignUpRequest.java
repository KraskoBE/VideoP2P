package com.krasen.web.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class SignUpRequest {

    @NotBlank
    @NotEmpty
    private String username;

    @NotBlank
    @NotEmpty
    private String firstName;

    @NotBlank
    @NotEmpty
    private String lastName;

    @NotBlank
    @NotEmpty
    private String password;

    @NotBlank
    @NotEmpty
    private String email;
}
