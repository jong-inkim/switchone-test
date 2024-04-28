package com.switchwon.user.service;

import com.switchwon.user.domain.User;
import com.switchwon.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(userId + ": 유저 아이디가 존재하지 않습니다."));
    }
}
