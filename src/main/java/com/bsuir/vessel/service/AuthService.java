package com.bsuir.vessel.service;

import com.bsuir.vessel.dto.request.LoginUserRequestDTO;
import com.bsuir.vessel.dto.request.UserRequestDTO;
import com.bsuir.vessel.dto.response.UserResponseDTO;
import com.bsuir.vessel.exception.model.*;
import com.bsuir.vessel.model.User;

public interface AuthService {

    User findByEmail(String username) throws EmailDontExistException;
    void registration(UserRequestDTO userRequestDTO) throws EmailExistException, PasswordException, EmailValidException;
    void validateCheckPassword(LoginUserRequestDTO userRequestDTO) throws PasswordException;

}
