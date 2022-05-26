package com.epam.rd.stock.exchange.service;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import com.epam.rd.stock.exchange.model.Valuable;

public interface UserValuableInfoService {
    UserValuableInfo getByUserAndValuable(User user, Valuable valuable);

    void delete(UserValuableInfo userValuableInfo);

    void update(UserValuableInfo userValuableInfo);

    void create(UserValuableInfo userValuableInfo);
}
