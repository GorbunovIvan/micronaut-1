package com.example.controller;

import com.example.model.User;
import com.example.service.UserService;
import io.micronaut.http.annotation.*;
import jakarta.inject.Inject;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    @Inject
    private UserService userService;

    @Get
    public List<User> getAll() {
        return userService.getAll();
    }

    @Get("/{id}")
    public User getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @Get("/name/{name}")
    public List<User> getAllByName(@PathVariable String name) {
        return userService.getAllByName(name);
    }

    @Get("/age/{age}")
    public List<User> getAllByAge(@PathVariable Integer age) {
        return userService.getAllByAge(age);
    }

    @Post
    public User create(@Body User user) {
        return userService.create(user);
    }

    @Put("/{id}")
    public User update(@PathVariable Integer id, @Body User user) {
        return userService.update(id, user);
    }

    @Delete("/{id}")
    public void delete(@PathVariable Integer id) {
        userService.delete(id);
    }
}
