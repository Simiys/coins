package com.example.coins.controller;


import com.example.coins.model.User;
import com.example.coins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/user")
    public ResponseEntity<User> getUserById(@RequestParam long id) {
        User user;
        Optional<User> opUser = userRepository.findByTgId(id);
        if(opUser.isPresent()) {
            user = opUser.get();
            return new ResponseEntity<User>(user, HttpStatusCode.valueOf(200));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getReferrals")
    public ResponseEntity<List<User>> getUserReferrals(@RequestParam long id) {
        Optional<User> opUser = userRepository.findByTgId(id);
        if (opUser.isPresent()) {
            List<User> referrals = userRepository.findAllByRefId(id);
            return new ResponseEntity<>(referrals, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
