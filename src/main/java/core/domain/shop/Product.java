package core.domain.shop;

import core.util.Constants;
import core.util.ValidationUtils;
import java.util.UUID;

/**
 * Abstract base class representing a product in the fitness club shop.
 * All products (clothes, supplements) extend this class.
 */
public abstract class Product implements ProductOperation {
    protected final String id;
    protected String name;
    protected double price;

    /**
     * Creates a new product with the specified name and price.
     *
     * @param name the name of the product (must not be null or blank)
     * @param price the price of the product (must be non-negative)
     * @throws IllegalArgumentException if name is null/blank or price is negative
     */
    public Product(String name, double price) {
        this.id = UUID.randomUUID().toString();
        this.name = ValidationUtils.requireNonBlank(name, "Назва продукту не може бути порожньою.");
        this.price = ValidationUtils.requireNonNegative(price, Constants.ERROR_PRODUCT_PRICE_NEGATIVE);
    }

    /**
     * Gets detailed information about the product.
     * Implementation is specific to each product type.
     *
     * @return a string containing product details
     */
    public abstract String getDetails();

    /**
     * Gets the unique identifier of the product.
     *
     * @return the product ID
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Gets the name of the product.
     *
     * @return the name
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the price of the product.
     *
     * @return the price
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * Compares this product with another object for equality.
     * Products are considered equal if they have the same ID.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    /**
     * Returns a hash code for this product.
     *
     * @return the hash code based on the product ID
     */
    @Override
    public int hashCode() {
        return id.hashCode();
    }
}