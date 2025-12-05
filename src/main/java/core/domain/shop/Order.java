package core.domain.shop;

import core.domain.client.Client;
import core.util.ValidationUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents an order placed by a client in the fitness club shop.
 * Contains order items and calculates the total price.
 */
public class Order {
    private final String id;
    private final Client client;
    private final List<OrderItem> items;
    private final LocalDateTime orderDate;
    private final double totalPrice;

    /**
     * Creates a new order for a client with the specified items.
     *
     * @param client the client placing the order (must not be null)
     * @param items the list of order items (must not be null or empty)
     * @throws IllegalArgumentException if client is null or items is null/empty
     */
    public Order(Client client, List<OrderItem> items) {
        this.id = UUID.randomUUID().toString();
        this.client = ValidationUtils.requireNonNull(client, "Клієнт не може бути null.");
        ValidationUtils.requireNonNull(items, "Список товарів не може бути null.");
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Замовлення не може бути порожнім.");
        }
        this.items = List.copyOf(items);
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

    /**
     * Gets the date and time when the order was placed.
     *
     * @return the order date
     */
    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    /**
     * Gets the total price of the order (after discounts).
     *
     * @return the total price after applying discounts
     */
    public double getTotalPrice() {
        return totalPrice;
    }
    
    /**
     * Calculates the original total price of all items before discounts.
     *
     * @return the original total price before discounts
     */
    public double getOriginalTotalPrice() {
        return items.stream()
                .mapToDouble(OrderItem::getOriginalTotalPrice)
                .sum();
    }
    
    /**
     * Calculates the total discount amount applied to the order.
     *
     * @return the total discount amount
     */
    public double getTotalDiscountAmount() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalDiscountAmount)
                .sum();
    }
}