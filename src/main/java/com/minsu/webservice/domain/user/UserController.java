package com.minsu.webservice.domain.user;

import com.minsu.webservice.domain.user.dto.CreateUserRequest;
import com.minsu.webservice.domain.user.dto.UserResponse;
import com.minsu.webservice.global.error.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.minsu.webservice.global.security.UserPrincipal;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> create(@RequestBody @Valid CreateUserRequest request) {
        UserResponse data = userService.create(request);
        return ApiResponse.ok("USER_CREATED", "User created", data);
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> get(@PathVariable Long id) {
        UserResponse data = userService.get(id);
        return ApiResponse.ok("USER_FOUND", "User found", data);
    }

    @PatchMapping("/{id}")
    public ApiResponse<UserResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid com.minsu.webservice.domain.user.dto.UpdateUserRequest request,
            @AuthenticationPrincipal com.minsu.webservice.global.security.UserPrincipal userPrincipal
    ) {
        UserResponse data = userService.update(id, request, userPrincipal);
        return ApiResponse.ok("USER_UPDATED", "User updated", data);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal com.minsu.webservice.global.security.UserPrincipal userPrincipal
    ) {
        userService.delete(id, userPrincipal);
        return ApiResponse.noContent("USER_DELETED", "User deleted");
    }
}
