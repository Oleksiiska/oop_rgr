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

    /**
     * Calculates the total price of all items in the order.
     *
     * @return the total price
     */
    private double calculateTotalPrice() {
        return items.stream()
                .mapToDouble(OrderItem::getTotalPrice)
                .sum();
    }

    /**
     * Gets the unique identifier of the order.
     *
     * @return the order ID
     */
    public String getId() {
        return id;
    }

    /**
     * Gets the client who placed the order.
     *
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * Gets the list of items in the order.
     *
     * @return an immutable copy of the items list
     */
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
     * Gets the total price of the order.
     *
     * @return the total price
     */
    public double getTotalPrice() {
        return totalPrice;
    }
}