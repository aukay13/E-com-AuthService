package com.ecommerce.authenticationservice.controllers;

import com.ecommerce.authenticationservice.DTOs.LoginRequestDTO;
import com.ecommerce.authenticationservice.DTOs.LoginResponseDTO;
import com.ecommerce.authenticationservice.DTOs.SignInRequestDTO;
import com.ecommerce.authenticationservice.DTOs.SignInResponseDTO;
import com.ecommerce.authenticationservice.Exceptions.*;
import com.ecommerce.authenticationservice.repositories.SessionRepository;
import com.ecommerce.authenticationservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @PostMapping("sign-up")
    public ResponseEntity<SignInResponseDTO> signUp(@RequestBody SignInRequestDTO signInRequestDTO) throws UserAlreadyExistsException {
        SignInResponseDTO responseDTO = new SignInResponseDTO();
//        try {
            if(authService.signUp(signInRequestDTO.getEmail(),signInRequestDTO.getPassword())){
                responseDTO.setResponseMessage("Sign Up Successful");
                return new ResponseEntity<>(responseDTO,HttpStatus.OK);
            }
            else {
                responseDTO.setResponseMessage("Sign up not successful");
                return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
            }

//        }
//        catch (Exception e){
//            responseDTO.setResponseMessage("Sign up Failed");
//            return new ResponseEntity<>(responseDTO,HttpStatus.FORBIDDEN);
//        }
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponseDTO> logIn(@RequestBody LoginRequestDTO loginRequestDTO) throws IncorrectPasswordException, NoUserFoundException {
        String jwt = authService.login(loginRequestDTO.getEmail(),loginRequestDTO.getPassword());
        LoginResponseDTO responseDTO = new LoginResponseDTO();
        responseDTO.setResponseMessage("Logged In");
        MultiValueMap<String,String> headers = new LinkedMultiValueMap<>();
        headers.add("AUTH_TOKEN",jwt);

        return new ResponseEntity<>(
                responseDTO,headers,HttpStatus.OK
        );
    }

    @GetMapping("validate")
    public ResponseEntity<String> validate(@RequestHeader("AUTH_TOKEN") String extractedJWT) throws InvalidTokenException, TokenExpiredException, UserAlreadyLoggedOutException {
        boolean isValidated = authService.validate(extractedJWT);
        if(isValidated){
            return new ResponseEntity<>(
                    "Token Validated",HttpStatus.ACCEPTED
            );
        }
        return new ResponseEntity<>(
                "Invalid token",HttpStatus.UNAUTHORIZED
        );
    }

    @GetMapping("logout")
    public ResponseEntity<String> logout(@RequestHeader("AUTH_TOKEN") String extractedJWT) throws InvalidTokenException, TokenExpiredException, UserAlreadyLoggedOutException {
        if(authService.logout(extractedJWT)){
            return new ResponseEntity<>("Logged out",HttpStatus.OK);
        }
        return new ResponseEntity<>("Could not logout. Try again",HttpStatus.INTERNAL_SERVER_ERROR);
    }
}