package com.epam.rd.stock.exchange.repository;


import com.epam.rd.stock.exchange.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
