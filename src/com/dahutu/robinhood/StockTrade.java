package com.dahutu.robinhood;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class StockTrade {
/*
Given a stream of incoming "buy" and "sell" orders (as lists of limit price, quantity, and side, like
["155", "3", "buy"]), determine the total quantity (or number of "shares") executed.

A "buy" order can be executed if there is a corresponding "sell" order with a price that is less than or
equal to the price of the "buy" order.
Similarly, a "sell" order can be executed if there is a corresponding "buy" order with a price that is
greater than or equal to the price of the "sell" order.
It is possible that an order does not execute immediately if it isn't paired to a counterparty. In that
case, you should keep track of that order and execute it at a later time when a pairing order is found.
You should ensure that orders are filled immediately at the best possible price. That is, an order
should be executed when it is processed, if possible. Further, "buy" orders should execute at the
lowest possible price and "sell" orders at the highest possible price at the time the order is handled.

Note that orders can be partially executed.

--- Sample Input ---

orders = [
  ['150', '5', 'buy'],    # Order A
  ['190', '1', 'sell'],   # Order B
  ['200', '1', 'sell'],   # Order C
  ['100', '9', 'buy'],    # Order D
  ['140', '8', 'sell'],   # Order E
  ['210', '4', 'buy'],    # Order F
]

Sample Output
9

[execution time limit] 3 seconds (java)

[input] array.array.string orders

[output] integer
*/
    public static final String[][] orders = {
        {"150", "5", "buy"},
        {"190", "1", "sell"},
        {"200", "1", "sell"},
        {"100", "9", "buy"},
        {"140", "8", "sell"},
        {"210", "4", "buy"}
    };

    public static enum OrderType {
        BUY,
        SELL,
    };

    public static class Order {
        int price;
        int shares;
        OrderType type;

        public Order(int price, int shares, OrderType type) {
            this.price = price;
            this.shares = shares;
            this.type = type;
        }

        public Order(String priceStr, String sharesStr, String typeStr) {
            this(Integer.parseInt(priceStr), Integer.parseInt(sharesStr),
                    "buy".equals(typeStr) ? OrderType.BUY : OrderType.SELL);
        }

        @Override
        public String toString() {
            return String.format("[price=%d, shares=%d, type=%s", price, shares, (type == OrderType.BUY ? "BUY" : "SELL"));
        }
    }

    public int getTotalSharesExecuted(String[][] orders) {
        List<Order> orderList = new ArrayList<>();
        for (String[] order : orders) {
            orderList.add(new Order(order[0], order[1], order[2]));
        }

        Queue<Order> buys = new PriorityQueue<>((a, b) -> b.price - a.price);
        Queue<Order> sells = new PriorityQueue<>((a, b) -> a.price - b.price);
        int total = 0;

        for (Order order : orderList) {
            if (order.type == OrderType.BUY) {
                while (!sells.isEmpty() && order.price >= sells.peek().price && order.shares > 0) {
                    Order sellOrder = sells.remove();
                    if (order.shares >= sellOrder.shares) {
                        total += sellOrder.shares;
                        order.shares -= sellOrder.shares;
                    } else {
                        total += order.shares;
                        sellOrder.shares -= order.shares;
                        sells.add(sellOrder);
                    }
                }
                if (order.shares > 0)
                    buys.add(order);
            } else {    // OrderType.SELL
                while (!buys.isEmpty() && order.price < buys.peek().price && order.shares > 0) {
                    Order buyOrder = buys.remove();
                    if (order.shares >= buyOrder.shares) {
                        total += buyOrder.shares;
                        order.shares -= buyOrder.shares;
                    } else {
                        total += order.shares;
                        buyOrder.shares -= order.shares;
                        buys.add(buyOrder);
                    }
                }
                if (order.shares > 0)
                    sells.add(order);
            }
        }

        return total;
    }

    public static void main(String[] args) {
        StockTrade s = new StockTrade();
        System.out.println(s.getTotalSharesExecuted(orders));
    }
}
