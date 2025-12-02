package core.domain.shop;

/**
 * Functional interface for creating products.
 * Used by the ProductFactory to create different types of products.
 */
@FunctionalInterface
public interface ProductCreator {
    /**
     * Creates a product with the specified parameters.
     *
     * @param name the name of the product
     * @param price the price of the product
     * @param params additional parameters specific to the product type
     * @return a new Product instance
     */
    Product create(String name, double price, String... params);
}

