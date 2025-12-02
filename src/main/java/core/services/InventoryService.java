package core.services;

import core.domain.shop.Inventory;
import core.domain.shop.ProductOperation;
import core.exceptions.ProductOutOfStockException;
import core.util.Constants;
import core.util.ValidationUtils;
import org.springframework.stereotype.Service;

/**
 * Service for managing inventory operations.
 * Provides a high-level interface for inventory management.
 */
@Service
public class InventoryService {
    
    private final Inventory inventory;
    
    /**
     * Creates a new inventory service with the specified inventory.
     *
     * @param inventory the inventory to manage (must not be null)
     * @throws IllegalArgumentException if inventory is null
     */
    public InventoryService(Inventory inventory) {
        this.inventory = ValidationUtils.requireNonNull(inventory, "Склад не може бути null.");
    }
    
    /**
     * Adds a product to the inventory.
     *
     * @param product the product to add (must not be null)
     * @param quantity the quantity to add (must be positive)
     * @throws IllegalArgumentException if product is null or quantity is not positive
     */
    public void addProduct(ProductOperation product, int quantity) {
        ValidationUtils.requireNonNull(product, Constants.ERROR_PRODUCT_NULL);
        inventory.addProduct(product, quantity);
    }
    
    /**
     * Removes a product from the inventory.
     *
     * @param product the product to remove (must not be null)
     * @param quantity the quantity to remove (must be positive)
     * @throws IllegalArgumentException if product is null or quantity is not positive
     * @throws ProductOutOfStockException if insufficient stock is available
     */
    public void removeProduct(ProductOperation product, int quantity) throws ProductOutOfStockException {
        ValidationUtils.requireNonNull(product, Constants.ERROR_PRODUCT_NULL);
        inventory.removeProduct(product, quantity);
    }
    
    /**
     * Checks if there is enough stock of a product.
     *
     * @param product the product to check
     * @param quantity the required quantity
     * @return true if sufficient stock is available, false otherwise
     */
    public boolean hasEnoughStock(ProductOperation product, int quantity) {
        if (product == null) {
            return false;
        }
        return inventory.hasEnoughStock(product, quantity);
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
        return inventory.getStockLevel(product);
    }
}

