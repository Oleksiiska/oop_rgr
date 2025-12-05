package core.domain.shop;

import core.util.Constants;
import core.util.ValidationUtils;

/**
 * Represents an item in an order.
 * Contains a product and the quantity ordered.
 *
 * @param product the product being ordered (must not be null)
 * @param quantity the quantity ordered (must be positive)
 */
public record OrderItem(Product product, int quantity) {
    
    /**
     * Creates a new order item.
     *
     * @param product the product being ordered (must not be null)
     * @param quantity the quantity ordered (must be positive)
     * @throws IllegalArgumentException if product is null or quantity is not positive
     */
    public OrderItem {
        ValidationUtils.requireNonNull(product, Constants.ERROR_PRODUCT_NULL);
        ValidationUtils.requirePositive(quantity, Constants.ERROR_PRODUCT_QUANTITY_INVALID);
    }

    /**
     * Calculates the total price for this order item (after discount).
     *
     * @return the total price (discounted product price * quantity)
     */
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
    
    /**
     * Calculates the original total price for this order item (before discount).
     *
     * @return the original total price (original product price * quantity)
     */
    public double getOriginalTotalPrice() {
        return product.getOriginalPrice() * quantity;
    }
    
    /**
     * Calculates the total discount amount for this order item.
     *
     * @return the total discount amount
     */
    public double getTotalDiscountAmount() {
        return product.getDiscountAmount() * quantity;
    }
}