package com.epam.rd.stock.exchange.repository;

import com.epam.rd.stock.exchange.model.Subscription;
import com.epam.rd.stock.exchange.model.Valuable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubscriptionRepository extends JpaRepository<Subscription,String> {
    List<Subscription> findByValuableId(String valuableId);
}
