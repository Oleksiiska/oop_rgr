package core.domain.shop;

import core.exceptions.ProductOutOfStockException;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<Product, Integer> stock;

    public Inventory() {
        this.stock = new HashMap<>();
    }

    public void addProduct(Product product, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Клькість завжди додатня.");
        }
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }

    public void removeProduct(Product product, int quantity) throws ProductOutOfStockException {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Клькість завжди додатня.");
        }
        int currentStock = getStockLevel(product);
        if (currentStock < quantity) {
            throw new ProductOutOfStockException(
                    "Недостатньо товару '" + product.getName() + "' на складі. В наявності: " + currentStock + ", запитано: " + quantity
            );
        }
        stock.put(product, currentStock - quantity);
    }

    public boolean hasEnoughStock(Product product, int quantity) {
        return getStockLevel(product) >= quantity;
    }

    public int getStockLevel(Product product) {
        return stock.getOrDefault(product, 0);
    }

    public Map<Product, Integer> getStock() {
        return Map.copyOf(stock);
    }
}