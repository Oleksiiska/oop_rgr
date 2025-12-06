package core.domain.shop;

/**
 * Strategy interface for calculating discounts on prices.
 * Different discount types (percentage, fixed amount, etc.) implement this interface.
 */
public interface DiscountOperation {
    /**
     * Calculates the discounted price from an original price.
     *
     * @param originalPrice the original price before discount (must be non-negative)
     * @return the price after applying the discount (must be non-negative)
     */
    double applyDiscount(double originalPrice);
    
    /**
     * Gets the discount amount (not the final price).
     *
     * @param originalPrice the original price before discount (must be non-negative)
     * @return the discount amount that was applied
     */
    double getDiscountAmount(double originalPrice);
    
    /**
     * Gets a description of the discount strategy.
     *
     * @return a human-readable description of the discount
     */
    String getDescription();
}
