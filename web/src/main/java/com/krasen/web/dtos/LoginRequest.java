package com.krasen.web.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank String username;

    @NotBlank String password;

}
