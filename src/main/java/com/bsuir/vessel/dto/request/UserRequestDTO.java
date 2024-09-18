package com.bsuir.vessel.dto.request;

import lombok.Data;

@Data
public class UserRequestDTO {

    private String email;
    private String password;
    private String name;
    private String surname;

}