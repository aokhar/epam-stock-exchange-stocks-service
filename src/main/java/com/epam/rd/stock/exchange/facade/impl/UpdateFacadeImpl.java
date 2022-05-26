package com.epam.rd.stock.exchange.facade.impl;

import com.epam.rd.stock.exchange.dto.ValuableUpdateDto;
import com.epam.rd.stock.exchange.facade.UpdateFacade;
import com.epam.rd.stock.exchange.model.*;
import com.epam.rd.stock.exchange.model.enums.OrderStatus;
import com.epam.rd.stock.exchange.model.enums.OrderType;
import com.epam.rd.stock.exchange.model.enums.SubscriptionType;
import com.epam.rd.stock.exchange.model.util.ConditionVerifyUtil;
import com.epam.rd.stock.exchange.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateFacadeImpl implements UpdateFacade {

    private final ValuableService valuableService;

    private final ValuableHistoryService valuableHistoryService;

    private final SubscriptionService subscriptionService;

    private final UserValuableInfoService userValuableInfoService;

    private final OrderService orderService;

    private final UserService userService;

    private final AlertService alertService;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void update(ValuableUpdateDto valuableUpdateDto) {
       Valuable valuable = valuableService.get(valuableUpdateDto.getSymbol());
       if(valuable.getPrice().compareTo(valuableUpdateDto.getPrice()) == 0){
           return;
       }
        BigDecimal trend = valuableUpdateDto.getPrice().subtract(valuable.getPrice());
       Valuable newValuable = Valuable.builder()
               .id(valuable.getId())
               .name(valuable.getName())
               .symbol(valuableUpdateDto.getSymbol())
               .price(valuableUpdateDto.getPrice())
               .trend(trend)
               .type(valuable.getType())
               .build();

        //Create history for update
        LocalDateTime now = LocalDateTime.now();
        ValuableHistory valuableHistory = ValuableHistory.builder()
                .valuableId(valuable.getId())
                .trend(trend)
                .dateTime(now)
                .previousPrice(valuable.getPrice())
                .newPrice(newValuable.getPrice())
                .build();

        valuableHistoryService.addHistory(valuableHistory);
        valuableService.updateValuable(newValuable);

        List<Subscription> subscriptionList = subscriptionService.findByValuableId(newValuable.getId());
        subscriptionList.parallelStream().filter(subscription -> this.checkSubscription(subscription, newValuable.getPrice()) )
                .forEach(subscription -> this.processSubscription(subscription, valuable));
    }

    private void processSubscription(Subscription subscription, Valuable valuable){
        if(subscription.getType().equals(SubscriptionType.INFORM)){
            performInform(subscription, valuable);
        }
        else if(subscription.getType().equals(SubscriptionType.SELL)){
            performSell(subscription, valuable);
        }
        else if(subscription.getType().equals(SubscriptionType.BUY)){
            performBuy(subscription, valuable);
        }
    }

    private void performInform(Subscription subscription, Valuable valuable){
        Alert alert = Alert.builder()
                .dateTime(LocalDateTime.now())
                .message(String.format("Alert for %s valuable %s condition : %s , current price is %s", valuable.getType(), valuable.getSymbol(), subscription.conditionToString(), valuable.getPrice()))
                .userId(subscription.getUserId())
                .build();
        alertService.createAlert(alert);
        if(!subscription.isContinuos()){
            subscriptionService.remove(subscription);
        }
    }

    private void performBuy(Subscription subscription, Valuable valuable){
        User user = userService.get(subscription.getUserId());
        Alert alert = Alert.builder()
                .dateTime(LocalDateTime.now())
                .userId(user.getId()).build();
        BigDecimal price = subscription.getAmount().multiply(valuable.getPrice());
        if(user.getBalance().subtract(price).compareTo(BigDecimal.ZERO) < 0){

            if(subscription.isFailSafe()){
                alert.setMessage(String.format("Subscription for %s valuable %s in count %s failed cause of not enough balance(without disable): %s",
                        valuable.getType(), valuable.getSymbol(), subscription.getAmount(), subscription.conditionToString()));
            }
            else{
                alert.setMessage(String.format("Subscription for %s valuable %s in count %s failed cause of not enough balance: %s",
                        valuable.getType(), valuable.getSymbol(), subscription.getAmount(), subscription.conditionToString()));
                subscriptionService.remove(subscription);
             }

        } else{
            alert.setMessage(String.format("Subscription for %s valuable %s in count %s success: %s , purchased %s valuables for %s with total %s",
                    valuable.getType(), valuable.getSymbol(), subscription.getAmount(), subscription.conditionToString(),
                    subscription.getAmount(), valuable.getPrice(), price));

            user.setBalance(user.getBalance().subtract(price));
            UserValuableInfo userValuableInfo = userValuableInfoService.getByUserAndValuable(user, valuable);
            if(userValuableInfo == null){
                userValuableInfo = UserValuableInfo.builder()
                        .valuable(valuable)
                        .user(user)
                        .amount(subscription.getAmount())
                        .sellAmount(BigDecimal.ZERO)
                        .build();
                userValuableInfoService.create(userValuableInfo);
            } else {
                userValuableInfo.setAmount(userValuableInfo.getAmount().add(subscription.getAmount()));
                userValuableInfoService.update(userValuableInfo);
            }
            Order order = Order.builder()
                    .dateTime(LocalDateTime.now())
                    .orderPrice(price)
                    .valuablePrice(valuable.getPrice())
                    .amount(subscription.getAmount())
                    .status(OrderStatus.SUCCESS)
                    .type(OrderType.AUTOMATIC_BUY)
                    .valuable(valuable)
                    .user(user)
                    .build();
            orderService.createOrder(order);
            subscriptionService.remove(subscription);
            userService.update(user);
        }
        alertService.createAlert(alert);
    }

    private void performSell(Subscription subscription, Valuable valuable){
        User user = userService.get(subscription.getUserId());
        Alert alert = Alert.builder()
                .dateTime(LocalDateTime.now())
                .userId(user.getId()).build();
        UserValuableInfo userValuableInfo = userValuableInfoService.getByUserAndValuable(user, valuable);
        BigDecimal price = subscription.getAmount().multiply(valuable.getPrice());
        if(userValuableInfo == null) {
            failSafeCheck(subscription, valuable, alert);
        }
        else{
            if(userValuableInfo.getAmount().subtract(subscription.getAmount()).compareTo(BigDecimal.ZERO)>=0){
                alert.setMessage(String.format("Subscription for %s valuable %s in count %s success: %s , selled %s valuables for %s with total ",
                        valuable.getType(), valuable.getSymbol(), subscription.getAmount(),
                        subscription.conditionToString(), subscription.getAmount(), valuable.getPrice(), price));
                user.setBalance(user.getBalance().add(price));

                Order order = Order.builder()
                        .dateTime(LocalDateTime.now())
                        .orderPrice(price)
                        .valuablePrice(valuable.getPrice())
                        .amount(subscription.getAmount())
                        .status(OrderStatus.SUCCESS)
                        .type(OrderType.AUTOMATIC_SELL)
                        .valuable(valuable)
                        .user(user)
                        .build();
                userValuableInfo.setAmount(userValuableInfo.getAmount().subtract(subscription.getAmount()));
                if(subscription.isReserve()){
                    userValuableInfo.setSellAmount(userValuableInfo.getSellAmount().subtract(subscription.getAmount()));
                }
                userValuableInfoService.update(userValuableInfo);
                userService.update(user);
                orderService.createOrder(order);
                subscriptionService.remove(subscription);
            }
            else{
                failSafeCheck(subscription, valuable, alert);
            }
        }
        alertService.createAlert(alert);

    }

    private void failSafeCheck(Subscription subscription, Valuable valuable, Alert alert) {
        if(subscription.isFailSafe()) {
            alert.setMessage(String.format("Subscription for %s valuable %s in count %s failed(without delete) cause of not enough valuables: %s",
                    valuable.getType(), valuable.getSymbol(), subscription.getAmount(), subscription.conditionToString()));
        } else{
            alert.setMessage(String.format("Subscription for %s valuable %s in count %s failed cause of not enough valuables: %s",
                    valuable.getType(), valuable.getSymbol(), subscription.getAmount(), subscription.conditionToString()));
            subscriptionService.remove(subscription);
        }
    }


    private boolean checkSubscription(Subscription subscription, BigDecimal price){
        return ConditionVerifyUtil.verify(subscription, price);
    }

}
