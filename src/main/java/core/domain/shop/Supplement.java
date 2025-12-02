package core.domain.shop;

import core.util.ValidationUtils;

/**
 * Represents a supplement product in the fitness club shop.
 * Supplements have a flavor in addition to name and price.
 */
public class Supplement extends Product {
    private final String flavor;

    /**
     * Creates a new supplement product.
     *
     * @param name the name of the supplement (must not be null or blank)
     * @param price the price of the supplement (must be non-negative)
     * @param flavor the flavor of the supplement (must not be null or blank)
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public Supplement(String name, double price, String flavor) {
        super(name, price);
        this.flavor = ValidationUtils.requireNonBlank(flavor, "Смак не може бути порожнім.");
    }

    /**
     * Gets detailed information about the supplement.
     *
     * @return a string containing the supplement details
     */
    @Override
    public String getDetails() {
        return "Добавка: " + name + ", Смак: " + flavor;
    }

    /**
     * Gets the flavor of the supplement.
     *
     * @return the flavor
     */
    public String getFlavor() {
        return flavor;
    }
}