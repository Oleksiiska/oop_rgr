package core.domain.shop;

import core.util.ValidationUtils;

/**
 * Represents a clothing item in the fitness club shop.
 * Clothing items have a size and color in addition to name and price.
 */
public class Clothes extends Product {
    private final String size;
    private final String color;

    /**
     * Creates a new clothing item.
     *
     * @param name the name of the clothing item (must not be null or blank)
     * @param price the price of the item (must be non-negative)
     * @param size the size of the item (must not be null or blank)
     * @param color the color of the item (must not be null or blank)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Clothes(String name, double price, String size, String color) {
        super(name, price);
        this.size = ValidationUtils.requireNonBlank(size, "Розмір не може бути порожнім.");
        this.color = ValidationUtils.requireNonBlank(color, "Колір не може бути порожнім.");
    }

    /**
     * Gets detailed information about the clothing item.
     *
     * @return a string containing the item details
     */
    @Override
    public String getDetails() {
        return "Одяг: " + name + ", Розмір: " + size + ", Колір: " + color;
    }

    /**
     * Gets the size of the clothing item.
     *
     * @return the size
     */
    public String getSize() {
        return size;
    }

    /**
     * Gets the color of the clothing item.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }
}