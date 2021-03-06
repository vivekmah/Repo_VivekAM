package com.spring.assignment.controller;

import com.spring.assignment.exception.UsersAndPostsNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UsersAndPostsNotFoundAdvice {
    @ExceptionHandler(value = UsersAndPostsNotFoundException.class)
    public ResponseEntity<Object> exception(UsersAndPostsNotFoundAdvice exception) {
        return new ResponseEntity<>("Users and their Posts not found", HttpStatus.NOT_FOUND);
    }
}
