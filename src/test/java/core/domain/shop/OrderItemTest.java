package core.domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderItemTest {
    private Product product;

    @BeforeEach
    void setUp() {
        ProductFactory factory = new ProductFactory();
        product = factory.createProduct("CLOSE", "Футболка", 500, "M", "Blue");
    }

    @Test
    void testOrderItemCreation() {
        OrderItem item = new OrderItem(product, 2);
        
        assertEquals(product, item.product());
        assertEquals(2, item.quantity());
    }

    @Test
    void testGetTotalPrice() {
        OrderItem item1 = new OrderItem(product, 1);
        assertEquals(500.0, item1.getTotalPrice(), 0.01);
        
        OrderItem item2 = new OrderItem(product, 3);
        assertEquals(1500.0, item2.getTotalPrice(), 0.01);
    }

    @Test
    void testOrderItemWithZeroQuantity() {
        assertThrows(IllegalArgumentException.class, () -> 
            new OrderItem(product, 0));
    }

    @Test
    void testOrderItemEquality() {
        OrderItem item1 = new OrderItem(product, 2);
        OrderItem item2 = new OrderItem(product, 2);
        
        // Records use structural equality
        assertEquals(item1, item2);
    }

    @Test
    void testOrderItemWithPercentageDiscount() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(20);
        product.setDiscountStrategy(discount);
        
        OrderItem item = new OrderItem(product, 2);
        
        // Original price: 500, with 20% discount: 400, for 2 items: 800
        assertEquals(800.0, item.getTotalPrice(), 0.01);
        assertEquals(1000.0, item.getOriginalTotalPrice(), 0.01); // 500 * 2
        assertEquals(200.0, item.getTotalDiscountAmount(), 0.01); // 100 * 2
    }

    @Test
    void testOrderItemWithFixedDiscount() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.fixedDiscount(100);
        product.setDiscountStrategy(discount);
        
        OrderItem item = new OrderItem(product, 3);
        
        // Original price: 500, with 100 discount: 400, for 3 items: 1200
        assertEquals(1200.0, item.getTotalPrice(), 0.01);
        assertEquals(1500.0, item.getOriginalTotalPrice(), 0.01); // 500 * 3
        assertEquals(300.0, item.getTotalDiscountAmount(), 0.01); // 100 * 3
    }

    @Test
    void testOrderItemOriginalTotalPrice() {
        OrderItem item = new OrderItem(product, 2);
        
        assertEquals(1000.0, item.getOriginalTotalPrice(), 0.01); // 500 * 2
        assertEquals(1000.0, item.getTotalPrice(), 0.01); // No discount
        assertEquals(0.0, item.getTotalDiscountAmount(), 0.01);
    }
}

