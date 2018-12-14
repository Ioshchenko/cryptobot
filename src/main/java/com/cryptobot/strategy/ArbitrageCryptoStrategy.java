package com.cryptobot.strategy;

import com.cryptobot.model.Order;
import com.cryptobot.model.OrderType;
import com.cryptobot.model.Ticker;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.RequestStatus;
import com.cryptobot.service.UserService;
import com.cryptobot.service.exmo.ExmoService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Log4j2
@Service
public class ArbitrageCryptoStrategy {

    private Calendar time = Calendar.getInstance();
    private boolean startStrategy = false;

    @Autowired
    private ExmoService exmoService;

    @Autowired
    private UserService userService;

    public void start(List<Ticker> tickers) {
/*        long time = Calendar.getInstance().getTimeInMillis() - this.time.getTimeInMillis();
        long minutes = TimeUnit.MILLISECONDS.toSeconds(time);
        if (minutes > 20 && !startStrategy) {
            startStrategy = true;

            Optional<ProfitPair> profitTicker = getProfitTicker(tickers);
            log.info(profitTicker);
            profitTicker.ifPresent(t -> {
                runStrategy(t.getName(), convert(tickers));

            });
            this.time = Calendar.getInstance();
            //startStrategy = false;
        }*/
        // Optional<ProfitTradingChain> profitTicker = getProfitTicker(tickers);
       /* profitTicker.ifPresent(t -> {
            TradingChain tradingChain = t.getTradingChain();
            List<String> pairs = Arrays.asList(tradingChain.getStartPair().getName(),
                    tradingChain.getMediumPair().getName(),
                    tradingChain.getEndPair().getName());

            Map<String, OrderBook> orderBooks = exmoService.getOrderBook(pairs);

            OrderBook orderBookStart = orderBooks.get(tradingChain.getStartPair().getName());
            List<Bet> betsForSale = orderBookStart.getBetsForSale();

            Bet bet = betsForSale.get(0);
            double coinsForBudget = getCountForBudget(bet.getPrice(), balanceUSD);

            double coins = getCountForCoins(coinsForBudget,tradingChain.getMediumPair().getPrice());
            double result = getCountForCoins(coins, tradingChain.getEndPair().getPrice());

            log.info(String.format("Expected: %f fast: %f", t.profit,result));

        });*/


    }

    private void runStrategy(TradingChain chain) {
        User user = userService.getDefaultUser();
        log.info("START");
        executeOrder(chain.getStartPair(), user, OrderType.BUY);
        executeOrder(chain.getMediumPair(), user, OrderType.BUY);
        executeOrder(chain.getEndPair(), user, OrderType.SELL);
        log.info("END");
    }


    private void executeOrder(Pair pair, User user, OrderType type) {
        Order order = Order.builder()
                .pair(pair.getName())
                .price(pair.getPrice())
                .quantity(pair.getQuantity() - 0.00000001)
                .type(type)
                .build();

        log.info("Create order: " + order);
        RequestStatus status = exmoService.createOrder(order, user);
        log.info(status);
        if (status.isResult()) {
            log.info("Order was created successfully");
            delay(1000);
            Map<String, List> userOpenOrders = exmoService.getUserOpenOrders(user);
            while (!userOpenOrders.isEmpty()) {
                delay(1000);
                userOpenOrders = exmoService.getUserOpenOrders(user);
            }
            log.info("Order executed ");
        } else {
            log.warn("ALERT !!!");
        }

    }

    private void delay(int delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            log.error(e);
        }
    }


}
