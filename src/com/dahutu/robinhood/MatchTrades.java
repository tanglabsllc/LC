package com.dahutu.robinhood;

import java.util.*;
import java.util.stream.Collectors;

public class MatchTrades {
    /*
    A trade is defined as a string containing the 4 properties given below separated by commas:

    Symbol (4 alphabetical characters, left-padded by spaces)
    Side (either "B" or "S" for buy or sell)
    Quantity (4 digits, left-padded by zeros)
    ID (6 alphanumeric characters)

    e.g.
    "AAPL,B,0100,ABC123"
    which represents a trade of a buy of 100 shares of AAPL with ID "ABC123"

    Given two lists of trades - called "house" and "street" trades, write code to create groups of matches between
    trades and return a list of unmatched house and street trades sorted alphabetically. Without any matching,
    the output list would contain all elements of both house and street trades. There are many ways to match trades,
    the first and most important way is an exact match:

    An exact match is a pair of trades containing exactly one house trade and exactly one street trade with identical
    symbol, side, quantity, and ID

    Note: Trades are distinct but not unique

    For example, given the following input:
    house_trades:
    [ "AAPL,B,0100,ABC123", "AAPL,B,0100,ABC123", "GOOG,S,0050,CDC333" ]
    street_trades:
    [ " FB,B,0100,GBGGGG", "AAPL,B,0100,ABC123" ]
    We would expect the following output:
    [ " FB,B,0100,GBGGGG", "AAPL,B,0100,ABC123", "GOOG,S,0050,CDC333" ]
    Because the first (or second) house trade and second street trade form an exact match, leaving behind three
    unmatched trades.

    Bonus 1 (Test 4 and 5): An attribute match is a match containing exactly one house trade and exactly one
    street trade with identical symbol, side, and quantity ignoring ID. Prioritize exact matches over attribute
    matches. Prioritize matching the earliest lexicographical house trade with the earliest lexicographical
    street trade in case of ties.

    Bonus 2: (Test 6) An offsetting match is a match containing exactly two house trades or exactly two street trades
    where the symbol and quantity of both trades are the same, but the side is different (one is a buy and one is
    a sell). Prioritize exact and attribute matches over offsetting matches. Prioritize matching the earliest
    lexicographical buy with the earliest lexicographical sell.
    */

    public static final String[] house  = {"AAPL,B,0100,ABC123", "AAPL,B,0100,ABC123", "GOOG,S,0050,CDC333"};
    public static final String[] street = {"  FB,B,0100,GBGGGG", "AAPL,B,0100,ABC123"};

    static class Order {
        String symbol;
        String side;
        String share;
        String id;
        String data;

        public Order(String trade) {
            String[] parts = trade.split(",");
            symbol = parts[0];
            side   = parts[1];
            share  = parts[2];
            id     = parts[3];
            data   = trade;
        }

        @Override
        public String toString() {
            return data;
        }

        public String getExactMatchKey() {
            return data;
        }

        public String getPartialMatchKey() {
            return String.format("%s,%s,%s", symbol, side, share);
        }

        public String getOffsetMatchKey() {
            return String.format("%s,%s,%s", symbol, "B".equals(side) ? "S" : "B", share);
        }
    }

    List<Order> convertToOrders(String[] trades) {
        List<Order> orders = new ArrayList<>();
        for (String trade : trades) {
            orders.add(new Order(trade));
        }
        return orders;
    }

    public List<String> getUnmatchedTrades(String[] house, String[] street) {
        Map<String, List<Order>> map = excludeMatch(convertToOrders(house), convertToOrders(street), true);

        map = excludeMatch(map.get("house"), map.get("street"), false);

        map.put("house", excludeOffset(map.get("house")));
        map.put("street", excludeOffset(map.get("street")));

        List<String> results = new ArrayList<>();
        for (String s : map.keySet()) {
            List<Order> orders = map.get(s);
            for (Order order : orders) {
                results.add(order.data);
            }
        }

        Collections.sort(results);

        return results;
    }

    // returns a map containing exclude matches for both "house" and "street"
    Map<String, List<Order>> excludeMatch(List<Order> houseOrders, List<Order> streetOrders, boolean isExactMatch) {
        Map<String, List<Order>> houseOrderMap = getOrderMap(houseOrders, isExactMatch);
        Map<String, List<Order>> streetOrderMap = getOrderMap(streetOrders, isExactMatch);

        Set<String> streetOrderKeys = streetOrderMap.keySet();
        for (String key : streetOrderKeys) {
            if (houseOrderMap.containsKey(key)) {
                List<Order> houseOrdersForKey = houseOrderMap.get(key);
                List<Order> streetOrdersForKey = streetOrderMap.get(key);
                int diff = houseOrdersForKey.size() - streetOrdersForKey.size();
                if (diff >= 0) {    // houseTradeCount >= streetTradeCount
                    houseOrderMap.put(key, houseOrdersForKey.subList(0, houseOrdersForKey.size() - Math.abs(diff)));
                    streetOrderMap.remove(key);
                } else {    // houseTradeCount < streetTradeCount
                    houseOrderMap.remove(key);
                    streetOrderMap.put(key, streetOrdersForKey.subList(0, streetOrdersForKey.size() - Math.abs(diff)));
                }
            }
        }

        Map<String, List<Order>> results = new HashMap<>();
        results.put("house", flattenValues(houseOrderMap));
        results.put("street", flattenValues(streetOrderMap));

        return results;
    }

    List<Order> excludeOffset(List<Order> orders) {
        Map<String, List<Order>> map = new HashMap<>();
        for (Order order : orders) {
            String partialKey = order.getPartialMatchKey();
            String offsetKey = order.getExactMatchKey();
            if (map.containsKey(offsetKey)) {
                List<Order> value = map.get(offsetKey);
                value.remove(0);
                if (value.isEmpty())
                    map.remove(offsetKey);
            } else {
                List<Order> value = map.getOrDefault(partialKey, new ArrayList<>());
                value.add(order);
                map.put(partialKey, value);
            }
        }

        return flattenValues(map);
    }

    Map<String, List<Order>> getOrderMap(List<Order> orders, boolean isExactMatch) {
        Map<String, List<Order>> orderMap = new HashMap<>();
        for (Order order : orders) {
            String key = isExactMatch ? order.getExactMatchKey() : order.getPartialMatchKey();
            List<Order> value = orderMap.getOrDefault(key, new ArrayList<Order>());
            value.add(order);
            orderMap.put(key, value);
        }

        return orderMap;
    }

    List<Order> flattenValues(Map<String, List<Order>> map) {
        List<Order> results = new ArrayList<>();
        for (List<Order> value : map.values()) {
            results.addAll(value);
        }
        return results;
    }

    public static void main(String[] args) {
        MatchTrades s = new MatchTrades();
        System.out.println(s.getUnmatchedTrades(house, street));
    }
}
