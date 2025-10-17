package src.core.domain.shop;

import src.core.domain.client.Client;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Order {
    private final String id;
    private final Client client;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private double totalPrice;

    public Order(Client client, List<OrderItem> items) {
        this.id = UUID.randomUUID().toString();
        this.client = client;
        this.items = items;
        this.orderDate = LocalDateTime.now();
        this.totalPrice = calculateTotalPrice();
    }

    private double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    public String getId() {
        return id;
    }

    public Client getClient() {
        return client;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}