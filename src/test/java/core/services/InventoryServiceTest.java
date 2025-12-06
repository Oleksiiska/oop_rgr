package core.services;

import core.domain.shop.Inventory;
import core.domain.shop.Product;
import core.domain.shop.ProductFactory;
import core.exceptions.ProductOutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {
    private InventoryService inventoryService;
    private Inventory inventory;
    private ProductFactory factory;
    private Product product;

    @BeforeEach
    void setUp() {
        inventory = new Inventory();
        inventoryService = new InventoryService(inventory);
        factory = new ProductFactory();
        product = factory.createProduct("CLOSE", "Футболка", 500, "M", "Blue");
    }

    @Test
    void testAddProduct() {
        inventoryService.addProduct(product, 10);

        assertEquals(10, inventoryService.getStockLevel(product));
    }

    @Test
    void testAddProductThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            inventoryService.addProduct(null, 10));
    }

    @Test
    void testRemoveProduct() throws ProductOutOfStockException {
        inventoryService.addProduct(product, 10);
        inventoryService.removeProduct(product, 5);

        assertEquals(5, inventoryService.getStockLevel(product));
    }

    @Test
    void testRemoveProductThrowsWhenNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            inventoryService.removeProduct(null, 5));
    }

    @Test
    void testRemoveProductThrowsWhenInsufficientStock() {
        inventoryService.addProduct(product, 5);

        assertThrows(ProductOutOfStockException.class, () -> 
            inventoryService.removeProduct(product, 10));
    }

    @Test
    void testHasEnoughStock() {
        inventoryService.addProduct(product, 10);

        assertTrue(inventoryService.hasEnoughStock(product, 5));
        assertTrue(inventoryService.hasEnoughStock(product, 10));
        assertFalse(inventoryService.hasEnoughStock(product, 11));
    }

    @Test
    void testHasEnoughStockReturnsFalseWhenProductNull() {
        assertFalse(inventoryService.hasEnoughStock(null, 5));
    }

    @Test
    void testGetStockLevel() {
        assertEquals(0, inventoryService.getStockLevel(product));

        inventoryService.addProduct(product, 7);
        assertEquals(7, inventoryService.getStockLevel(product));
    }

    @Test
    void testGetStockLevelReturnsZeroWhenProductNull() {
        assertEquals(0, inventoryService.getStockLevel(null));
    }

    @Test
    void testMultipleProducts() throws ProductOutOfStockException {
        Product product2 = factory.createProduct("SUPPLEMENT", "Протеїн", 1200, "Шоколад");

        inventoryService.addProduct(product, 10);
        inventoryService.addProduct(product2, 5);

        assertEquals(10, inventoryService.getStockLevel(product));
        assertEquals(5, inventoryService.getStockLevel(product2));

        inventoryService.removeProduct(product, 3);
        inventoryService.removeProduct(product2, 2);

        assertEquals(7, inventoryService.getStockLevel(product));
        assertEquals(3, inventoryService.getStockLevel(product2));
    }
}

