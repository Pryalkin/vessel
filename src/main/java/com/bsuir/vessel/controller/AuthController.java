package com.bsuir.vessel.controller;

import com.bsuir.vessel.constant.HttpAnswer;
import com.bsuir.vessel.dto.request.LoginUserRequestDTO;
import com.bsuir.vessel.dto.request.UserRequestDTO;
import com.bsuir.vessel.dto.response.HttpResponse;
import com.bsuir.vessel.dto.response.UserResponseDTO;
import com.bsuir.vessel.exception.ExceptionHandling;
import com.bsuir.vessel.exception.model.EmailDontExistException;
import com.bsuir.vessel.exception.model.EmailExistException;
import com.bsuir.vessel.exception.model.EmailValidException;
import com.bsuir.vessel.exception.model.PasswordException;
import com.bsuir.vessel.model.User;
import com.bsuir.vessel.model.UserPrincipal;
import com.bsuir.vessel.service.AuthService;
import com.bsuir.vessel.utility.JWTTokenProvider;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bsuir.vessel.constant.HttpAnswer.USER_SUCCESSFULLY_REGISTERED;
import static com.bsuir.vessel.constant.SecurityConstant.JWT_TOKEN_HEADER;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController extends ExceptionHandling {

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;
    private final JWTTokenProvider jwtTokenProvider;

    @PostMapping("/registration")
    public ResponseEntity<HttpResponse> registration(@RequestBody UserRequestDTO userRequestDTO) throws EmailExistException, PasswordException, EmailValidException {
        authService.registration(userRequestDTO);
        return HttpAnswer.response(CREATED, USER_SUCCESSFULLY_REGISTERED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@RequestBody LoginUserRequestDTO userRequestDTO) throws EmailDontExistException, PasswordException {
        authService.validateCheckPassword(userRequestDTO);
        authenticate(userRequestDTO.getEmail(), userRequestDTO.getPassword());
        User loginUser = authService.findByEmail(userRequestDTO.getEmail());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        UserResponseDTO userResponseDTO = new UserResponseDTO();
        userResponseDTO.setEmail(loginUser.getEmail());
        userResponseDTO.setName(loginUser.getName());
        userResponseDTO.setSurname(loginUser.getSurname());
        userResponseDTO.setRole(loginUser.getRole());
        userResponseDTO.setAuthorities(loginUser.getAuthorities());
        return new ResponseEntity<>(userResponseDTO, jwtHeader, OK);
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }

    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));
        return headers;
    }


}
