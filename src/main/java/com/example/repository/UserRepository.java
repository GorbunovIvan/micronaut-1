package com.example.repository;

import com.example.model.User;
import io.micronaut.data.annotation.Query;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    List<User> findByNameContains(String name);

    @Query("FROM User WHERE age = :age")
    List<User> findByAge(Integer age);
}
