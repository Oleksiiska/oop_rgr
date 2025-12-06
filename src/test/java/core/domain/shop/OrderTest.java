package core.domain.shop;

import core.domain.client.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrderTest {
    private Client client;
    private Product product1;
    private Product product2;
    private ProductFactory factory;

    @BeforeEach
    void setUp() {
        client = new Client("Олена Ковальчук", "+380991234567");
        factory = new ProductFactory();
        product1 = factory.createProduct("CLOSE", "Футболка", 500, "M", "Blue");
        product2 = factory.createProduct("SUPPLEMENT", "Протеїн", 1200, "Шоколад");
    }

    @Test
    void testOrderCreation() {
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 2),
                new OrderItem(product2, 1)
        );
        
        Order order = new Order(client, items);
        
        assertNotNull(order.getId());
        assertEquals(client, order.getClient());
        assertEquals(2, order.getItems().size());
        assertNotNull(order.getOrderDate());
        assertEquals(2200.0, order.getTotalPrice(), 0.01); // 500*2 + 1200*1
    }

    @Test
    void testOrderTotalPrice() {
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 3),
                new OrderItem(product2, 2)
        );
        
        Order order = new Order(client, items);
        
        double expectedTotal = (500 * 3) + (1200 * 2);
        assertEquals(expectedTotal, order.getTotalPrice(), 0.01);
    }

    @Test
    void testOrderGetItemsReturnsImmutableCopy() {
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 1)
        );
        
        Order order = new Order(client, items);
        List<OrderItem> orderItems = order.getItems();
        
        assertThrows(UnsupportedOperationException.class, () -> 
            orderItems.add(new OrderItem(product2, 1)));
    }

    @Test
    void testOrderWithSingleItem() {
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 1)
        );
        
        Order order = new Order(client, items);
        
        assertEquals(1, order.getItems().size());
        assertEquals(500.0, order.getTotalPrice(), 0.01);
    }

    @Test
    void testOrderDateIsSet() {
        LocalDateTime before = LocalDateTime.now();
        
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 1)
        );
        Order order = new Order(client, items);
        
        LocalDateTime after = LocalDateTime.now();
        
        assertTrue(order.getOrderDate().isAfter(before.minusSeconds(1)));
        assertTrue(order.getOrderDate().isBefore(after.plusSeconds(1)));
    }

    @Test
    void testOrderWithDiscounts() {
        // Apply discounts to products
        product1.setDiscountStrategy(DiscountStrategy.percentageDiscount(20)); // 500 -> 400
        product2.setDiscountStrategy(DiscountStrategy.percentageDiscount(10)); // 1200 -> 1080
        
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 2), // 400 * 2 = 800
                new OrderItem(product2, 1)  // 1080 * 1 = 1080
        );
        
        Order order = new Order(client, items);
        
        // Total with discounts: 800 + 1080 = 1880
        assertEquals(1880.0, order.getTotalPrice(), 0.01);
        
        // Original total: (500 * 2) + (1200 * 1) = 2200
        assertEquals(2200.0, order.getOriginalTotalPrice(), 0.01);
        
        // Total discount: (100 * 2) + (120 * 1) = 320
        assertEquals(320.0, order.getTotalDiscountAmount(), 0.01);
    }

    @Test
    void testOrderWithMixedDiscounts() {
        product1.setDiscountStrategy(DiscountStrategy.fixedDiscount(50)); // 500 -> 450
        product2.setDiscountStrategy(DiscountStrategy.percentageDiscount(15)); // 1200 -> 1020
        
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 3), // 450 * 3 = 1350
                new OrderItem(product2, 2)  // 1020 * 2 = 2040
        );
        
        Order order = new Order(client, items);
        
        assertEquals(3390.0, order.getTotalPrice(), 0.01);
        assertEquals(3900.0, order.getOriginalTotalPrice(), 0.01); // (500*3) + (1200*2)
        assertEquals(510.0, order.getTotalDiscountAmount(), 0.01); // (50*3) + (180*2)
    }

    @Test
    void testOrderOriginalTotalPrice() {
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 2),
                new OrderItem(product2, 1)
        );
        
        Order order = new Order(client, items);
        
        // No discounts applied
        assertEquals(2200.0, order.getOriginalTotalPrice(), 0.01);
        assertEquals(2200.0, order.getTotalPrice(), 0.01);
        assertEquals(0.0, order.getTotalDiscountAmount(), 0.01);
    }

    @Test
    void testOrderDiscountAmountCalculation() {
        product1.setDiscountStrategy(DiscountStrategy.percentageDiscount(30));
        
        List<OrderItem> items = Arrays.asList(
                new OrderItem(product1, 5) // 350 * 5 = 1750
        );
        
        Order order = new Order(client, items);
        
        double originalTotal = 500.0 * 5; // 2500
        double discountAmount = 150.0 * 5; // 750 (30% of 500 = 150 per item)
        double finalTotal = originalTotal - discountAmount; // 1750
        
        assertEquals(finalTotal, order.getTotalPrice(), 0.01);
        assertEquals(originalTotal, order.getOriginalTotalPrice(), 0.01);
        assertEquals(discountAmount, order.getTotalDiscountAmount(), 0.01);
    }
}

