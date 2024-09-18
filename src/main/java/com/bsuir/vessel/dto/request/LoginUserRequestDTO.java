package com.bsuir.vessel.dto.request;

import lombok.Data;

@Data
public class LoginUserRequestDTO {

    private String email;
    private String password;
    private String password2;

}