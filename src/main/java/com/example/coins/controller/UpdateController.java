package com.example.coins.controller;

import com.example.coins.model.User;
import com.example.coins.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RequestMapping("/api")
@RestController
public class UpdateController {

    @Autowired
    private UserRepository userRepository;

    private Optional<User> findUserByTgId(long tgId) {
        return userRepository.findByTgId(tgId);
    }

    @PatchMapping("/collect") // Returns amount of collected coins
    public ResponseEntity<Long> collectFarmedCoins(@RequestParam long id) {
        System.out.println("collect " + id);
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            long  coins = user.getEarnedCoins();
            if ((int) Math.floor(100 * Math.random()) >= 95) { // Jackpot chance calculation
                user.setCoins(user.getCoins() + user.getEarnedCoins() + 500);
                coins += 500;
            } else {
                user.setCoins(user.getCoins() + user.getEarnedCoins());

            }
            user.setLastFarmStart(null);
            userRepository.save(user);
            // Calculating bonuses for referrals
            f (user.getRefId() != 0) {
                Optional<User> ref1Opt = userRepository.findByTgId(user.getRefId());
                if (ref1Opt.isPresent()) {
                    User ref1 = ref1Opt.get();
                    ref1.setCoinsFromRefs((long) Math.floor(coins * 0.1) + ref1.getCoinsFromRefs());
                    userRepository.save(ref1);
                    Optional<User> ref2Opt = userRepository.findByTgId(ref1.getRefId());
                    if (ref2Opt.isPresent()) {
                        User ref2 = ref2Opt.get();
                        ref2.setCoinsFromRefs((long) Math.floor(coins * 0.025) + ref2.getCoinsFromRefs());
                        userRepository.save(ref2);
                    }
                }
            }
            return new ResponseEntity<>(coins, HttpStatus.OK);
        }
        return new ResponseEntity<>((long) -100, HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/reflink")
    public ResponseEntity<HttpStatus> updateReferralLinkStatus(@RequestParam long id) {
        System.out.println("reflink " + id);
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setRefLinkStatus(true);
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK ,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND);
    }

    @PatchMapping("/farmStart")
    public ResponseEntity<Integer> updateFarmTime(@RequestParam long id) {
        System.out.println("farmStart " + id);
        Optional<User> opUser = findUserByTgId(id);
        if (opUser.isPresent()) {
            User user = opUser.get();
            user.setLastFarmStart(LocalDateTime.now());
            int coins = (int) Math.floor(30 * Math.random());
            coins += 50; // Randomizing earned coins from 50 to 80
            user.setEarnedCoins(coins);
            userRepository.save(user);
            return new ResponseEntity<>(coins, HttpStatus.OK);
        }
        return new ResponseEntity<>((int) -100, HttpStatus.NOT_FOUND);
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
            return new ResponseEntity<>(HttpStatus.OK,HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND,HttpStatus.NOT_FOUND);
    }
}
