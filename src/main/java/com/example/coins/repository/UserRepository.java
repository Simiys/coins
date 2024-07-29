package com.example.coins.repository;


import com.example.coins.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByTgId(long tgId);

    List<User> findAllByRefId(long refId);



}