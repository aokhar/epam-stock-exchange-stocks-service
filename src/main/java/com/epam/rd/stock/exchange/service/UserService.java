package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.User;

public interface UserService {
    User get(String userId);

    void update(User user);
}
