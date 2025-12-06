package core.domain.shop;

import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Strategy implementations for different discount types.
 * Provides various discount calculation strategies for products and memberships.
 */
public class DiscountStrategy {
    
    /**
     * No discount strategy - returns the original price.
     */
    public static class NoDiscount implements DiscountOperation {
        @Override
        public double applyDiscount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            return originalPrice;
        }
        
        @Override
        public double getDiscountAmount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            return 0.0;
        }
        
        @Override
        public String getDescription() {
            return "Без знижки";
        }
    }
    
    /**
     * Percentage discount strategy.
     * Applies a percentage discount to the original price.
     */
    public static class PercentageDiscount implements DiscountOperation {
        private final double discountPercent;
        
        /**
         * Creates a percentage discount strategy.
         *
         * @param discountPercent the discount percentage (must be between 0 and 100)
         * @throws IllegalArgumentException if discountPercent is not in valid range
         */
        public PercentageDiscount(double discountPercent) {
            if (discountPercent < 0 || discountPercent > 100) {
                throw new IllegalArgumentException("Відсоток знижки повинен бути від 0 до 100.");
            }
            this.discountPercent = discountPercent;
        }
        
        @Override
        public double applyDiscount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            double discountAmount = getDiscountAmount(originalPrice);
            double finalPrice = originalPrice - discountAmount;
            return Math.max(0.0, finalPrice); // Ensure price doesn't go negative
        }
        
        @Override
        public double getDiscountAmount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            return originalPrice * (discountPercent / 100.0);
        }
        
        @Override
        public String getDescription() {
            return String.format("Знижка %d%%", (int) discountPercent);
        }
        
        /**
         * Gets the discount percentage.
         *
         * @return the discount percentage
         */
        public double getDiscountPercent() {
            return discountPercent;
        }
    }
    
    /**
     * Fixed amount discount strategy.
     * Applies a fixed discount amount to the original price.
     */
    public static class FixedDiscount implements DiscountOperation {
        private final double discountAmount;
        
        /**
         * Creates a fixed discount strategy.
         *
         * @param discountAmount the fixed discount amount (must be non-negative)
         * @throws IllegalArgumentException if discountAmount is negative
         */
        public FixedDiscount(double discountAmount) {
            if (discountAmount < 0) {
                throw new IllegalArgumentException("Сума знижки не може бути від'ємною.");
            }
            this.discountAmount = discountAmount;
        }
        
        @Override
        public double applyDiscount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            double finalPrice = originalPrice - discountAmount;
            return Math.max(0.0, finalPrice); // Ensure price doesn't go negative
        }
        
        @Override
        public double getDiscountAmount(double originalPrice) {
            ValidationUtils.requireNonNegative(originalPrice, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
            return Math.min(discountAmount, originalPrice); // Don't discount more than the price
        }
        
        @Override
        public String getDescription() {
            return String.format("Фіксована знижка %.2f грн", discountAmount);
        }
        
        /**
         * Gets the fixed discount amount.
         *
         * @return the discount amount
         */
        public double getDiscountAmount() {
            return discountAmount;
        }
    }
    
    /**
     * Gets a no-discount strategy instance.
     *
     * @return a NoDiscount strategy instance
     */
    public static DiscountOperation noDiscount() {
        return new NoDiscount();
    }
    
    /**
     * Creates a percentage discount strategy.
     *
     * @param percent the discount percentage (0-100)
     * @return a PercentageDiscount strategy instance
     */
    public static DiscountOperation percentageDiscount(double percent) {
        return new PercentageDiscount(percent);
    }
    
    /**
     * Creates a fixed amount discount strategy.
     *
     * @param amount the fixed discount amount
     * @return a FixedDiscount strategy instance
     */
    public static DiscountOperation fixedDiscount(double amount) {
        return new FixedDiscount(amount);
    }
}
