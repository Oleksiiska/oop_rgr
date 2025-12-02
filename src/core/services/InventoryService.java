package core.services;

import core.domain.shop.Inventory;
import core.domain.shop.ProductOperation;
import core.exceptions.ProductOutOfStockException;

public class InventoryService {
    
    private final Inventory inventory;
    
    public InventoryService(Inventory inventory) {
        this.inventory = inventory;
    }
    
    public void addProduct(ProductOperation product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не може бути null.");
        }
        inventory.addProduct(product, quantity);
    }
    
    public void removeProduct(ProductOperation product, int quantity) throws ProductOutOfStockException {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не може бути null.");
        }
        inventory.removeProduct(product, quantity);
    }
    
    public boolean hasEnoughStock(ProductOperation product, int quantity) {
        if (product == null) {
            return false;
        }
        return inventory.hasEnoughStock(product, quantity);
    }
    
    public int getStockLevel(ProductOperation product) {
        if (product == null) {
            return 0;
        }
        return inventory.getStockLevel(product);
    }
}

