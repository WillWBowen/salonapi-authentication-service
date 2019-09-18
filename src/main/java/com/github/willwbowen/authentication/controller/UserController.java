package com.github.willwbowen.authentication.controller;

import com.github.willwbowen.authentication.dto.UserDto;
import com.github.willwbowen.authentication.dto.UserRegistrationDto;
import com.github.willwbowen.authentication.model.User;
import com.github.willwbowen.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/current")
    public Principal getUser(Principal principal) {
        return principal;
    }

    @PostMapping
    @PreAuthorize("#oauth2.hasScope('server')")
    public UserDto createUser(@Valid @RequestBody UserRegistrationDto userRegistration) {
        User savedUser = userService.create(toUser(userRegistration));
        return toDto(savedUser);
    }

    private UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    private User toUser(UserRegistrationDto userRegistration) {
        User user = new User();
        user.setUsername(userRegistration.getUsername());
        user.setPassword(userRegistration.getPassword());
        return user;
    }
}
