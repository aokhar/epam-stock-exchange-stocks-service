package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.repository.UserRepository;
import com.epam.rd.stock.exchange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User get(String userId) {
        return userRepository.findById(userId).get();
    }

    @Override
    public void update(User user) {
        userRepository.save(user);
    }
}
