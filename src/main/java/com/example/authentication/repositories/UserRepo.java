package com.example.authentication.repositories;

import org.springframework.data.repository.CrudRepository;

import com.example.authentication.models.User;

public interface UserRepo extends CrudRepository<User,Long> {
    
    User findByEmail(String email);
}
