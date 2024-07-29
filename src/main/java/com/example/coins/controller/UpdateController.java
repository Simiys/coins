package com.example.coins.controller;

import com.example.coins.model.User;
import com.example.coins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("/api/update")
@RestController
public class UpdateController {

    @Autowired
    private UserRepository userRepository;

    private Optional<User> findUserByTgId(long tgId) {
        return userRepository.findByTgId(tgId);
    }

    @PatchMapping("/coins")
    public ResponseEntity<HttpStatus> updateCoinsCount(@RequestParam long coins, @RequestParam long tgId) {
        Optional<User> opUser = findUserByTgId(tgId);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setCoins(user.getCoins() + coins);
            if (user.getRefId() != 0) {
                userRepository.findByTgId(user.getRefId()).ifPresent(firstRef -> {
                    firstRef.setCoinsFromRefs(firstRef.getCoinsFromRefs() + Math.round(0.1 * coins));
                    if (firstRef.getRefId() != 0) {
                        userRepository.findByTgId(firstRef.getRefId()).ifPresent(secondRef ->
                                secondRef.setCoinsFromRefs(secondRef.getCoinsFromRefs() + Math.round(0.025 * coins))
                        );
                    }
                });
            }
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/reflink")
    public ResponseEntity<HttpStatus> updateReferralLinkStatus(@RequestParam long id) {
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setRefLinkStatus(true);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/farmStart")
    public ResponseEntity<HttpStatus> updateFarmTime(@RequestParam long id) {
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setLastFarmStart(LocalDateTime.now());
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/claimRefCoins")
    public ResponseEntity<HttpStatus> claimRefCoins(@RequestParam long id) {
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setCoins(user.getCoins() + user.getCoinsFromRefs());
            user.setCoinsFromRefs(0);
            user.setLastRefClaim(LocalDateTime.now());
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
