package com.example.service;

import com.example.model.Address;
import com.example.model.User;
import com.example.repository.UserRepository;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MicronautTest
class UserServiceTest {

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @MockBean(UserRepository.class)
    UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Test
    void shouldReturnListWhenGetAll() {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 22, null)
        );

        when(userRepository.findAll()).thenReturn(users);

        var result = userService.getAll();
        assertEquals(users, result);

        verify(userRepository, only()).findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldReturnUserWhenGetById() {

        var id = 1;

        var user = new User(id, "test 1", 11, null);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        var result = userService.getById(id);
        assertEquals(user, result);

        verify(userRepository, only()).findById(id);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnNullWhenGetById() {

        var id = 1;

        when(userRepository.findById(anyInt())).thenReturn(Optional.empty());

        var result = userService.getById(id);
        assertNull(result);

        verify(userRepository, only()).findById(id);
        verify(userRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnListWithOneUserWhenGeAllByName() {

        var users = List.of(
                new User(1, "test 1", 11, null)
        );

        when(userRepository.findByNameContains(anyString())).thenReturn(users);

        var result = userService.getAllByName("test");
        assertEquals(users, result);

        verify(userRepository, only()).findByNameContains("test");
        verify(userRepository, times(1)).findByNameContains("test");
    }

    @Test
    void shouldReturnListWithTwoUsersWhenGeAllByName() {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 22, null)
        );

        when(userRepository.findByNameContains(anyString())).thenReturn(users);

        var result = userService.getAllByName("test");
        assertEquals(users, result);

        verify(userRepository, only()).findByNameContains("test");
        verify(userRepository, times(1)).findByNameContains("test");
    }

    @Test
    void shouldReturnEmptyListWhenGeAllByName() {

        when(userRepository.findByNameContains(anyString())).thenReturn(Collections.emptyList());

        var result = userService.getAllByName("test");
        assertTrue(result.isEmpty());

        verify(userRepository, only()).findByNameContains("test");
        verify(userRepository, times(1)).findByNameContains("test");
    }

    @Test
    void shouldReturnListWithOneUserWhenGeAllByAge() {

        var users = List.of(
                new User(1, "test 1", 11, null)
        );

        when(userRepository.findByAge(anyInt())).thenReturn(users);

        var result = userService.getAllByAge(11);
        assertEquals(users, result);

        verify(userRepository, only()).findByAge(11);
        verify(userRepository, times(1)).findByAge(11);
    }

    @Test
    void shouldReturnListWithTwoUsersWhenGeAllByAge() {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 11, null)
        );

        when(userRepository.findByAge(anyInt())).thenReturn(users);

        var result = userService.getAllByAge(11);
        assertEquals(users, result);

        verify(userRepository, only()).findByAge(11);
        verify(userRepository, times(1)).findByAge(11);
    }

    @Test
    void shouldReturnEmptyListWhenGeAllByAge() {

        when(userRepository.findByAge(anyInt())).thenReturn(Collections.emptyList());

        var result = userService.getAllByAge(11);
        assertTrue(result.isEmpty());

        verify(userRepository, only()).findByAge(11);
        verify(userRepository, times(1)).findByAge(11);
    }

    @Test
    void shouldReturnUserWithIdWhenCreate() {

        when(userRepository.save(any(User.class))).thenAnswer(answer -> {
            User userToCreate = answer.getArgument(0);
            userToCreate.setId(99);
            if (userToCreate.getAddress() != null) {
                userToCreate.getAddress().setId(99);
            }
            return userToCreate;
        });

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        var result = userService.create(user);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(user, result);
        assertEquals(user.getAddress(), result.getAddress());

        verify(userRepository, only()).save(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void shouldReturnUserWhenUpdate() {

        var id = 1;

        when(userRepository.existsById(id)).thenReturn(true);
        when(userRepository.update(any(User.class))).thenAnswer(answer -> answer.getArgument(0));

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        var result = userService.update(id, user);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(user, result);
        assertEquals(user.getAddress(), result.getAddress());

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).update(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdate() {

        var id = 1;

        when(userRepository.existsById(id)).thenReturn(false);
        when(userRepository.update(any(User.class))).thenAnswer(answer -> answer.getArgument(0));

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        assertThrows(RuntimeException.class, () -> userService.update(id, user));

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).update(any());
    }

    @Test
    void testDelete() {
        var id = 1;
        userService.delete(id);
        verify(userRepository, only()).deleteById(id);
        verify(userRepository, times(1)).deleteById(id);
    }
}