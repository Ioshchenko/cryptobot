package com.cryptobot.service.exmo;

import com.cryptobot.model.Exchange;
import com.cryptobot.model.ExchangeKey;
import com.cryptobot.model.Order;
import com.cryptobot.model.User;
import com.cryptobot.model.exmo.Bet;
import com.cryptobot.model.exmo.OrderBook;
import com.cryptobot.model.exmo.RequestStatus;
import com.cryptobot.model.exmo.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExmoService {

    @Autowired
    private ExmoAuthApi exmoAuthApi;

    @Autowired
    private ExmoApi exmoApi;

    public UserInfo getUserInfo(User user) {
        ExchangeKey exchangeKey = user.getExchangeKey().get(Exchange.EXMO);
        return (UserInfo) exmoAuthApi.request("/user_info", exchangeKey, UserInfo.class);
    }

    public RequestStatus createOrder(Order order, User user) {
        ExchangeKey exchangeKey = user.getExchangeKey().get(Exchange.EXMO);
        Map<String, Object> params = convertToMap(order);
        return (RequestStatus) exmoAuthApi.request("/order_create", exchangeKey, RequestStatus.class, params);
    }

    public Map<String, List> getUserOpenOrders(User user) {
        ExchangeKey exchangeKey = user.getExchangeKey().get(Exchange.EXMO);
        return (Map<String, List>) exmoAuthApi.request("/user_open_orders", exchangeKey, Map.class);
    }

    public Map<String, OrderBook> getOrderBook(List<String> pairs) {
        Map<String, Map<String, Object>> orderBook = exmoApi.getOrderBook(pairs);

        return orderBook.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> convertToOrderBook(e.getValue())));
    }

    private OrderBook convertToOrderBook(Map<String, Object> data) {
        List<Bet> bid = converts((List<List<String>>) data.get("bid"));
        List<Bet> ask = converts((List<List<String>>) data.get("ask"));

        OrderBook orderBook = new OrderBook();
        orderBook.setBetsForPurchase(bid);
        orderBook.setBetsForSale(ask);

        return orderBook;
    }

    private List<Bet> converts(List<List<String>> bids) {
        return bids.stream()
                .map(this::convertToBet)
                .collect(Collectors.toList());
    }

    private Bet convertToBet(List<String> l) {
        return new Bet(
                Double.valueOf(l.get(0)),
                Double.valueOf(l.get(1)),
                Double.valueOf(l.get(2)));
    }

    private Map<String, Object> convertToMap(Order order) {
        Map<String, Object> params = new HashMap<>();
        params.put("pair", order.getPair());
        params.put("quantity", order.getQuantity());
        params.put("price", order.getPrice());
        params.put("type", order.getType().getValue());
        return params;
    }
}
