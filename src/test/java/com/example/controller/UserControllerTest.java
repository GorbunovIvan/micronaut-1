package com.example.controller;

import com.example.model.Address;
import com.example.model.User;
import com.example.service.UserService;
import io.micronaut.http.MediaType;
import io.micronaut.serde.ObjectMapper;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@MicronautTest
class UserControllerTest {

    @Inject
    private RequestSpecification spec;

    @Inject
    UserService userService;

    @MockBean(UserService.class)
    UserService userService() {
        return mock(UserService.class, withSettings().withoutAnnotations());
    }

    @Inject
    private ObjectMapper objectMapper;

    private static final String BASE_PATH = "/api/v1/users";

    @Test
    void shouldReturnListWhenGetAll() throws IOException {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 22, null)
        );

        when(userService.getAll()).thenReturn(users);

        var jsonExpected = objectMapper.writeValueAsString(users);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get()
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAll();
        verify(userService, times(1)).getAll();
    }

    @Test
    void shouldReturnUserWhenGetById() throws IOException {

        var id = 1;

        var user = new User(id, "test 1", 11, null);

        when(userService.getById(id)).thenReturn(user);

        var jsonExpected = objectMapper.writeValueAsString(user);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getById(id);
        verify(userService, times(1)).getById(id);
    }

    @Test
    void shouldReturnNullWhenGetById() {

        var id = 1;

        when(userService.getById(anyInt())).thenReturn(null);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/{id}", id)
                .then()
                .statusCode(404);

        verify(userService, only()).getById(id);
        verify(userService, times(1)).getById(id);
    }

    @Test
    void shouldReturnListWithOneUserWhenGeAllByName() throws IOException {

        var users = List.of(
                new User(1, "test 1", 11, null)
        );

        when(userService.getAllByName(anyString())).thenReturn(users);

        var jsonExpected = objectMapper.writeValueAsString(users);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/name/{name}", "test")
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAllByName("test");
        verify(userService, times(1)).getAllByName("test");
    }

    @Test
    void shouldReturnListWithTwoUsersWhenGeAllByName() throws IOException {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 22, null)
        );

        when(userService.getAllByName(anyString())).thenReturn(users);

        var jsonExpected = objectMapper.writeValueAsString(users);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/name/{name}", "test")
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAllByName("test");
        verify(userService, times(1)).getAllByName("test");
    }

    @Test
    void shouldReturnEmptyListWhenGeAllByName() throws IOException {

        when(userService.getAllByName(anyString())).thenReturn(Collections.emptyList());

        var jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/name/{name}", "test")
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAllByName("test");
        verify(userService, times(1)).getAllByName("test");
    }

    @Test
    void shouldReturnListWithOneUserWhenGeAllByAge() throws IOException {

        var users = List.of(
                new User(1, "test 1", 11, null)
        );

        when(userService.getAllByAge(anyInt())).thenReturn(users);

        var jsonExpected = objectMapper.writeValueAsString(users);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/age/{age}", 11)
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        var result = userService.getAllByAge(11);
        assertEquals(users, result);

        verify(userService, atLeastOnce()).getAllByAge(11);
    }

    @Test
    void shouldReturnListWithTwoUsersWhenGeAllByAge() throws IOException {

        var users = List.of(
                new User(1, "test 1", 11, null),
                new User(2, "test 2", 11, null)
        );

        when(userService.getAllByAge(anyInt())).thenReturn(users);

        var jsonExpected = objectMapper.writeValueAsString(users);

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/age/{age}", 11)
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAllByAge(11);
        verify(userService, times(1)).getAllByAge(11);
    }

    @Test
    void shouldReturnEmptyListWhenGeAllByAge() throws IOException {

        when(userService.getAllByAge(anyInt())).thenReturn(Collections.emptyList());

        var jsonExpected = objectMapper.writeValueAsString(Collections.emptyList());

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .get("/age/{age}", 11)
                .then()
                .statusCode(200)
                .body(equalTo(jsonExpected));

        verify(userService, only()).getAllByAge(11);
        verify(userService, times(1)).getAllByAge(11);
    }

    @Test
    void shouldReturnUserWithIdWhenCreate() throws IOException {

        when(userService.create(any(User.class))).thenAnswer(answer -> {
            User userToCreate = answer.getArgument(0);
            userToCreate.setId(99);
            if (userToCreate.getAddress() != null) {
                userToCreate.getAddress().setId(99);
            }
            return userToCreate;
        });

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        var jsonRequest = objectMapper.writeValueAsString(user);

        var jsonResponse = spec.given()
                .basePath(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonRequest)
                .when()
                .post()
                .then()
                .extract()
                .asPrettyString();

        var userResponse = objectMapper.readValue(jsonResponse, User.class);

        assertNotNull(userResponse);
        assertNotNull(userResponse.getId());
        assertEquals(user, userResponse);
        assertEquals(user.getAddress(), userResponse.getAddress());

        verify(userService, only()).create(user);
        verify(userService, times(1)).create(user);
    }

    @Test
    void shouldReturnUserWhenUpdate() throws IOException {

        var id = 1;

        when(userService.update(anyInt(), any(User.class))).thenAnswer(answer -> {
            int idParam = answer.getArgument(0);
            User userParam = answer.getArgument(1);
            userParam.setId(idParam);
            return userParam;
        });

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        var jsonRequest = objectMapper.writeValueAsString(user);

        var jsonResponse = spec.given()
                .basePath(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonRequest)
                .when()
                .put("/{id}", id)
                .then()
                .extract()
                .asPrettyString();

        var userResponse = objectMapper.readValue(jsonResponse, User.class);

        assertNotNull(userResponse);
        assertEquals(id, userResponse.getId());
        assertEquals(user, userResponse);
        assertEquals(user.getAddress(), userResponse.getAddress());

        verify(userService, times(1)).update(anyInt(), any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdate() throws IOException {

        var id = 1;

        when(userService.update(anyInt(), any(User.class))).thenAnswer(answer -> {
            int idParam = answer.getArgument(0);
            User userParam = answer.getArgument(1);
            userParam.setId(idParam);
            return userParam;
        });

        var address = new Address(null, "country", "city", "street", 15);
        var user = new User(null, "new user", 21, address);

        var jsonRequest = objectMapper.writeValueAsString(user);

        spec.given()
                .basePath(BASE_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonRequest)
                .when()
                .put("/{id}", id)
                .then()
                .statusCode(200);

        verify(userService, times(1)).update(anyInt(), any());
    }

    @Test
    void testDelete() {

        var id = 1;

        spec.given()
                .basePath(BASE_PATH)
                .when()
                .delete("/{id}", id)
                .then()
                .statusCode(200);

        verify(userService, only()).delete(id);
        verify(userService, times(1)).delete(id);
    }
}