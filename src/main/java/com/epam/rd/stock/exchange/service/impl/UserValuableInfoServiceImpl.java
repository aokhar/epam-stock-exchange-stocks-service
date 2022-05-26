package com.epam.rd.stock.exchange.service.impl;

import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import com.epam.rd.stock.exchange.model.Valuable;
import com.epam.rd.stock.exchange.repository.UserValuableInfoRepository;
import com.epam.rd.stock.exchange.service.UserValuableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserValuableInfoServiceImpl implements UserValuableInfoService {

    private final UserValuableInfoRepository userValuableInfoRepository;

    @Override
    public UserValuableInfo getByUserAndValuable(User user, Valuable valuable) {
        return userValuableInfoRepository.findUserValuableInfoByUserAndValuable(user, valuable);
    }

    @Override
    public void delete(UserValuableInfo userValuableInfo) {
        userValuableInfoRepository.delete(userValuableInfo);
    }

    @Override
    public void update(UserValuableInfo userValuableInfo) {
        userValuableInfoRepository.save(userValuableInfo);
    }

    @Override
    public void create(UserValuableInfo userValuableInfo) {
        userValuableInfoRepository.save(userValuableInfo);
    }
}
