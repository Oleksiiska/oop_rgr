package core.domain.shop;

/**
 * Interface defining operations available on products.
 * Provides access to product information and details.
 */
public interface ProductOperation {
    /**
     * Gets the unique identifier of the product.
     *
     * @return the product ID
     */
    String getId();
    
    /**
     * Gets the name of the product.
     *
     * @return the name
     */
    String getName();
    
    /**
     * Gets the price of the product.
     *
     * @return the price
     */
    double getPrice();
    
    /**
     * Gets detailed information about the product.
     *
     * @return a string containing product details
     */
    String getDetails();
}

