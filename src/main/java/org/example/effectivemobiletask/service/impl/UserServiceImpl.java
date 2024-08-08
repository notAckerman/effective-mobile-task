package org.example.effectivemobiletask.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.effectivemobiletask.model.entity.User;
import org.example.effectivemobiletask.repository.UserRepository;
import org.example.effectivemobiletask.service.UserService;
import org.example.effectivemobiletask.util.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public User getUser(String name) {
        return userRepository.findByName(name).orElseThrow(
                () -> new NotFoundException("User not found")
        );
    }
}
