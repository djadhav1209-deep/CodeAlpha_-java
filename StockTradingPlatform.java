import java.util.*;

class Stock {
    String name;
    double price;

    Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    double balance = 10000; // starting money

    void buyStock(Stock stock, int quantity) {
        double cost = stock.price * quantity;

        if (cost > balance) {
            System.out.println("Not enough balance.");
            return;
        }

        balance -= cost;
        holdings.put(stock.name, holdings.getOrDefault(stock.name, 0) + quantity);

        System.out.println("Bought " + quantity + " shares of " + stock.name);
    }

    void sellStock(Stock stock, int quantity) {
        int owned = holdings.getOrDefault(stock.name, 0);

        if (quantity > owned) {
            System.out.println("Not enough shares to sell.");
            return;
        }

        balance += stock.price * quantity;
        holdings.put(stock.name, owned - quantity);

        System.out.println("Sold " + quantity + " shares of " + stock.name);
    }

    void showPortfolio(Map<String, Stock> market) {
        double totalValue = balance;

        System.out.println("\n--- Portfolio ---");
        System.out.println("Cash Balance: $" + balance);

        for (String stockName : holdings.keySet()) {
            int qty = holdings.get(stockName);
            double value = market.get(stockName).price * qty;

            System.out.println(stockName + " : " + qty + " shares | Value $" + value);
            totalValue += value;
        }

        System.out.println("Total Portfolio Value: $" + totalValue);
    }
}

public class StockTradingPlatform {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Map<String, Stock> market = new HashMap<>();
        market.put("AAPL", new Stock("AAPL", 180));
        market.put("GOOGL", new Stock("GOOGL", 140));
        market.put("TSLA", new Stock("TSLA", 250));

        Portfolio portfolio = new Portfolio();

        while (true) {
            System.out.println("\n--- Stock Trading Platform ---");
            System.out.println("1. Show Market Data");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. Exit");

            System.out.print("Choose option: ");
            int choice = sc.nextInt();

            if (choice == 1) {
                System.out.println("\nMarket Data:");
                for (Stock s : market.values()) {
                    System.out.println(s.name + " : $" + s.price);
                }
            }

            else if (choice == 2) {
                System.out.print("Enter stock name: ");
                String name = sc.next();

                System.out.print("Enter quantity: ");
                int qty = sc.nextInt();

                if (market.containsKey(name)) {
                    portfolio.buyStock(market.get(name), qty);
                } else {
                    System.out.println("Stock not found.");
                }
            }

            else if (choice == 3) {
                System.out.print("Enter stock name: ");
                String name = sc.next();

                System.out.print("Enter quantity: ");
                int qty = sc.nextInt();

                if (market.containsKey(name)) {
                    portfolio.sellStock(market.get(name), qty);
                } else {
                    System.out.println("Stock not found.");
                }
            }

            else if (choice == 4) {
                portfolio.showPortfolio(market);
            }

            else if (choice == 5) {
                System.out.println("Exiting...");
                break;
            }
        }

        sc.close();
    }
}