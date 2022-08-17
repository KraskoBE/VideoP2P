package com.krasen.web.dtos;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginRequest {

    @NotBlank String username;

    @NotBlank String password;

}
