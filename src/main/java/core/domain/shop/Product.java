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
    protected DiscountOperation discountStrategy;

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
        this.discountStrategy = DiscountStrategy.noDiscount();
    }

    public abstract String getDetails();

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public double getOriginalPrice() {
        return price;
    }
    
    @Override
    public double getPrice() {
        if (discountStrategy == null) {
            return price;
        }
        return discountStrategy.applyDiscount(price);
    }
    
    public double getDiscountAmount() {
        if (discountStrategy == null) {
            return 0.0;
        }
        return discountStrategy.getDiscountAmount(price);
    }
    
    public void setDiscountStrategy(DiscountOperation discountStrategy) {
        this.discountStrategy = discountStrategy != null 
            ? discountStrategy 
            : DiscountStrategy.noDiscount();
    }
    
    public DiscountOperation getDiscountStrategy() {
        return discountStrategy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return id.equals(product.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}