package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import io.micronaut.transaction.annotation.Transactional;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Singleton
@RequiredArgsConstructor
public class UserService {

    @Inject
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public List<User> getAllByName(String name) {
        return userRepository.findByNameContains(name);
    }

    public List<User> getAllByAge(Integer age) {
        return userRepository.findByAge(age);
    }

    public User create(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public User update(Integer id, User user) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User with id " + id + " not found");
        }
        user.setId(id);
        return userRepository.update(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }
}
