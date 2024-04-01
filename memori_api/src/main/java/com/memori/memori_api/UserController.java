package com.memori.memori_api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.memori.memori_api.requests.UserCreationRequest;
import com.memori.memori_api.requests.UserPatchRequest;
import com.memori.memori_api.responses.UserCreationResponse;
import com.memori.memori_domain.User.Tier;
import com.memori.memori_service.dtos.UserDto;
import com.memori.memori_service.services.UserService;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = "/api/user", produces = "application/json")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UserCreationResponse> handleCustomException(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(UserCreationResponse
                        .builder()
                        .message("An error occurred: " + ex.getMessage())
                        .build());
    }

    @PostMapping
    public ResponseEntity<UserCreationResponse> createUser(@Valid @RequestBody UserCreationRequest request,
            BindingResult result, HttpServletRequest httpRequest) {
        if (result.hasErrors()) {
            UserCreationResponse resp = UserCreationResponse.builder()
                    .message("Invalid request: " + result.getAllErrors()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        UserDto userDto = new UserDto();
        userDto.setEmail(request.getEmail());
        userDto.setUsername(request.getUsername());
        userDto.setId(httpRequest.getAttribute("uid").toString());
        userDto.setIsEmailVerified(request.getIsEmailVerified());
        userDto.setStorageSizeInByte(0L);
        userDto.setTier(Tier.BASIC.getValue());
        userDto.setDailyResetTime(2);
        userDto.setTimezone("Asia/Singapore");
        try {
            userService.createUser(userDto);
        }
        catch (EntityExistsException e) {
        }

        return ResponseEntity.ok().body(UserCreationResponse
                .builder()
                .message("The user is created successfully.")
                .build());
    }

    @PatchMapping
    public ResponseEntity<UserCreationResponse> patchUser(@Valid @RequestBody UserPatchRequest request,
            BindingResult result, HttpServletRequest httpRequest) {
        if (result.hasErrors()) {
            UserCreationResponse resp = UserCreationResponse.builder()
                    .message("Invalid request: " + result.getAllErrors()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
        }

        UserDto userDto = userService.getUserById(httpRequest.getAttribute("uid").toString())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        if (request.getEmail() != null)
            userDto.setEmail(request.getEmail());
        if (request.getUsername() != null)
            userDto.setUsername(request.getUsername());
        if (request.getIsEmailVerified() != null)
            userDto.setIsEmailVerified(request.getIsEmailVerified());
        if (request.getSetStorageSizeInByte() != null)
            userDto.setStorageSizeInByte(request.getSetStorageSizeInByte());
        if (request.getDailyResetTime() != null)
            userDto.setDailyResetTime(request.getDailyResetTime());
        if (request.getTimezone() != null)
            userDto.setTimezone(request.getTimezone());
        userService.updateUser(userDto);

        return ResponseEntity.ok().body(UserCreationResponse
                .builder()
                .message("The user is created successfully.")
                .build());
    }
}
