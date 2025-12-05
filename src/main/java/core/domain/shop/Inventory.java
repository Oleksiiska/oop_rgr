package core.domain.shop;

import org.springframework.stereotype.Component;

import core.event.Observer;
import core.exceptions.ProductOutOfStockException;
import core.event.Event;
import core.event.Observable;
import core.util.Constants;
import core.util.ValidationUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the inventory of products in the fitness club shop.
 * Tracks stock levels and notifies observers of low stock events.
 */
@Component
public class Inventory {
    private final Map<ProductOperation, Integer> stock;
    private final Observable<Event> eventObservable;

    /**
     * Creates a new empty inventory.
     */
    public Inventory() {
        this.stock = new HashMap<>();
        this.eventObservable = new Observable<>();
    }
    
    public void addObserver(Observer<Event> observer) {
        eventObservable.addObserver(observer);
    }
    
    public void removeObserver(Observer<Event> observer) {
        eventObservable.removeObserver(observer);
    }

    /**
     * Adds a product to the inventory with the specified quantity.
     *
     * @param product the product to add (must not be null)
     * @param quantity the quantity to add (must be positive)
     * @throws IllegalArgumentException if product is null or quantity is not positive
     */
    public void addProduct(ProductOperation product, int quantity) {
        ValidationUtils.requireNonNull(product, Constants.ERROR_PRODUCT_NULL);
        ValidationUtils.requirePositive(quantity, Constants.ERROR_PRODUCT_QUANTITY_INVALID);
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }

    /**
     * Removes a product from the inventory with the specified quantity.
     * Throws an exception if insufficient stock is available.
     * Notifies observers if stock falls below the low stock threshold.
     *
     * @param product the product to remove (must not be null)
     * @param quantity the quantity to remove (must be positive)
     * @throws IllegalArgumentException if product is null or quantity is not positive
     * @throws ProductOutOfStockException if insufficient stock is available
     */
    public void removeProduct(ProductOperation product, int quantity) throws ProductOutOfStockException {
        ValidationUtils.requireNonNull(product, Constants.ERROR_PRODUCT_NULL);
        ValidationUtils.requirePositive(quantity, Constants.ERROR_PRODUCT_QUANTITY_INVALID);
        
        int currentStock = getStockLevel(product);
        if (currentStock < quantity) {
            throw new ProductOutOfStockException(
                    "Недостатньо товару '" + product.getName() + "' на складі. В наявності: " + currentStock + ", запитано: " + quantity
            );
        }
        int newStock = currentStock - quantity;
        stock.put(product, newStock);
        
        if (newStock <= Constants.LOW_STOCK_THRESHOLD && newStock > 0) {
            eventObservable.notifyObservers(new Event(Event.EventType.LOW_INVENTORY, 
                "Низький залишок товару '" + product.getName() + "': " + newStock + " од.", this));
        }
    }

    /**
     * Checks if there is enough stock of a product.
     *
     * @param product the product to check (must not be null)
     * @param quantity the required quantity
     * @return true if sufficient stock is available, false otherwise
     */
    public boolean hasEnoughStock(ProductOperation product, int quantity) {
        if (product == null) {
            return false;
        }
        return getStockLevel(product) >= quantity;
    }

    /**
     * Gets the current stock level of a product.
     *
     * @param product the product to check
     * @return the stock level, or 0 if product is null or not in stock
     */
    public int getStockLevel(ProductOperation product) {
        if (product == null) {
            return 0;
        }
        return stock.getOrDefault(product, 0);
    }

    /**
     * Gets a copy of the entire stock map.
     *
     * @return an immutable copy of the stock map
     */
    public Map<ProductOperation, Integer> getStock() {
        return Map.copyOf(stock);
    }
}