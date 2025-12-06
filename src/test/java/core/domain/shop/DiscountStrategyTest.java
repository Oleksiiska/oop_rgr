package core.domain.shop;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiscountStrategyTest {
    
    @Test
    void testNoDiscount() {
        core.domain.shop.DiscountOperation noDiscount = DiscountStrategy.noDiscount();
        
        double originalPrice = 1000.0;
        assertEquals(1000.0, noDiscount.applyDiscount(originalPrice), 0.01);
        assertEquals(0.0, noDiscount.getDiscountAmount(originalPrice), 0.01);
        assertTrue(noDiscount.getDescription().contains("Без знижки"));
    }
    
    @Test
    void testPercentageDiscount() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(20);
        
        double originalPrice = 1000.0;
        double expectedDiscount = 200.0; // 20% of 1000
        double expectedFinal = 800.0;
        
        assertEquals(expectedFinal, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(expectedDiscount, discount.getDiscountAmount(originalPrice), 0.01);
        assertTrue(discount.getDescription().contains("20%"));
    }
    
    @Test
    void testPercentageDiscountZeroPercent() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(0);
        
        double originalPrice = 1000.0;
        assertEquals(1000.0, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(0.0, discount.getDiscountAmount(originalPrice), 0.01);
    }
    
    @Test
    void testPercentageDiscountHundredPercent() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(100);
        
        double originalPrice = 1000.0;
        assertEquals(0.0, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(1000.0, discount.getDiscountAmount(originalPrice), 0.01);
    }
    
    @Test
    void testPercentageDiscountInvalidPercentNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            DiscountStrategy.percentageDiscount(-10));
    }
    
    @Test
    void testPercentageDiscountInvalidPercentOver100() {
        assertThrows(IllegalArgumentException.class, () -> 
            DiscountStrategy.percentageDiscount(150));
    }
    
    @Test
    void testFixedDiscount() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.fixedDiscount(100);
        
        double originalPrice = 1000.0;
        double expectedFinal = 900.0;
        
        assertEquals(expectedFinal, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(100.0, discount.getDiscountAmount(originalPrice), 0.01);
        assertTrue(discount.getDescription().contains("100"));
    }
    
    @Test
    void testFixedDiscountLargerThanPrice() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.fixedDiscount(1500);
        
        double originalPrice = 1000.0;
        // Discount shouldn't make price negative
        assertEquals(0.0, discount.applyDiscount(originalPrice), 0.01);
        // Discount amount should be capped at the price
        assertEquals(1000.0, discount.getDiscountAmount(originalPrice), 0.01);
    }
    
    @Test
    void testFixedDiscountInvalidNegative() {
        assertThrows(IllegalArgumentException.class, () -> 
            DiscountStrategy.fixedDiscount(-50));
    }
    
    @Test
    void testFixedDiscountZero() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.fixedDiscount(0);
        
        double originalPrice = 1000.0;
        assertEquals(1000.0, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(0.0, discount.getDiscountAmount(originalPrice), 0.01);
    }
    
    @Test
    void testPercentageDiscountWithDecimalPrice() {
        core.domain.shop.DiscountOperation discount = DiscountStrategy.percentageDiscount(15);
        
        double originalPrice = 1234.56;
        double expectedDiscount = 185.184; // 15% of 1234.56
        double expectedFinal = 1049.376;
        
        assertEquals(expectedFinal, discount.applyDiscount(originalPrice), 0.01);
        assertEquals(expectedDiscount, discount.getDiscountAmount(originalPrice), 0.01);
    }
    
    @Test
    void testDiscountStrategyDescription() {
        core.domain.shop.DiscountOperation noDiscount = DiscountStrategy.noDiscount();
        core.domain.shop.DiscountOperation percentage = DiscountStrategy.percentageDiscount(25);
        core.domain.shop.DiscountOperation fixed = DiscountStrategy.fixedDiscount(50);
        
        assertNotNull(noDiscount.getDescription());
        assertNotNull(percentage.getDescription());
        assertNotNull(fixed.getDescription());
        assertFalse(noDiscount.getDescription().isEmpty());
        assertFalse(percentage.getDescription().isEmpty());
        assertFalse(fixed.getDescription().isEmpty());
    }
}
