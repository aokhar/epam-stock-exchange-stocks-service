package com.epam.rd.stock.exchange.repository;


import com.epam.rd.stock.exchange.model.User;
import com.epam.rd.stock.exchange.model.UserValuableInfo;
import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserValuableInfoRepository extends JpaRepository<UserValuableInfo, String> {
    UserValuableInfo findUserValuableInfoByUserAndValuable(User user, Valuable valuable);
}
