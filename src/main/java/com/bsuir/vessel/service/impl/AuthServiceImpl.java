package com.bsuir.vessel.service.impl;

import com.bsuir.vessel.dto.request.LoginUserRequestDTO;
import com.bsuir.vessel.dto.request.UserRequestDTO;
import com.bsuir.vessel.exception.model.EmailDontExistException;
import com.bsuir.vessel.exception.model.EmailExistException;
import com.bsuir.vessel.exception.model.EmailValidException;
import com.bsuir.vessel.exception.model.PasswordException;
import com.bsuir.vessel.mapper.UserMapper;
import com.bsuir.vessel.model.User;
import com.bsuir.vessel.model.UserPrincipal;
import com.bsuir.vessel.repository.UserRepository;
import com.bsuir.vessel.service.AuthService;
import com.bsuir.vessel.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

import static com.bsuir.vessel.constant.UserImplConstant.*;
import static com.bsuir.vessel.enumeration.Role.ROLE_ADMIN;
import static com.bsuir.vessel.enumeration.Role.ROLE_USER;

@Service
@Qualifier("userDetailsService")
@AllArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JWTTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(NO_USER_FOUND_BY_USERNAME + email));
        UserPrincipal userPrincipal = new UserPrincipal(user);
        log.info(FOUND_USER_BY_USERNAME + email);
        return userPrincipal;
    }

    @Override
    public void registration(UserRequestDTO userRequestDTO) throws EmailExistException, PasswordException, EmailValidException {
        validateEmail(userRequestDTO);
        validatePassword(userRequestDTO);
        User user = userMapper.userRequestDtoToUser(userRequestDTO);
        user.setPassword(encodePassword(userRequestDTO.getPassword()));
        if (userRepository.findAll().isEmpty()) {
            user.setRole(ROLE_ADMIN.name());
            user.setAuthorities(ROLE_ADMIN.getAuthorities());
        } else {
            user.setRole(ROLE_USER.name());
            user.setAuthorities(ROLE_USER.getAuthorities());
        }
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String username) throws EmailDontExistException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EmailDontExistException(EMAIL_DONT_ALREADY_EXISTS));
    }

    private void validateEmail(UserRequestDTO userRequestDTO) throws EmailExistException, EmailValidException {
        if (userRepository.findByEmail(userRequestDTO.getEmail()).isPresent()){
            throw new EmailExistException(EMAIL_ALREADY_EXISTS);
        }
        Pattern VALID_EMAIL_ADDRESS_REGEX =
                Pattern.compile("^[a-zA-Z0-9_+&-]+(?:\\.[a-zA-Z0-9_+&-]+)@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        if (VALID_EMAIL_ADDRESS_REGEX.matcher(userRequestDTO.getEmail()).matches()){
            throw new EmailValidException(EMAIL_IS_NOT_VALID);
        }
    }

    private void validatePassword(UserRequestDTO userRequestDTO) throws PasswordException {
        String regex = "^(?=.[a-z])(?=.[A-Z])(?=.[0-9])(?=.[\\w|\\W])";
        if (userRequestDTO.getPassword().matches(regex)) {
            throw new PasswordException(PASSWORD_IS_NOT_VALID);
        }
    }

    public void validateCheckPassword(LoginUserRequestDTO loginUserRequestDTO) throws PasswordException {
        if (!loginUserRequestDTO.getPassword().equals(loginUserRequestDTO.getPassword2())){
            throw new PasswordException(PASSWORD_IS_NOT_VALID);
        }
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

}
