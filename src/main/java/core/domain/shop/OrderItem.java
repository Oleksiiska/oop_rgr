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
     * Calculates the total price for this order item.
     *
     * @return the total price (product price * quantity)
     */
    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}