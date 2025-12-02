package core.domain.shop;

import core.util.ValidationUtils;

/**
 * Enumeration of product types available in the fitness club shop.
 */
public enum ProductType {
    CLOTHES("CLOSE"),
    SUPPLEMENT("SUPPLEMENT");
    
    private final String code;
    
    /**
     * Creates a product type with the specified code.
     *
     * @param code the code string for this product type
     */
    ProductType(String code) {
        this.code = code;
    }
    
    /**
     * Gets the code string for this product type.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
    
    /**
     * Converts a code string to a ProductType.
     *
     * @param code the code string to convert (must not be null or blank)
     * @return the corresponding ProductType
     * @throws IllegalArgumentException if the code is null, blank, or unknown
     */
    public static ProductType fromCode(String code) {
        ValidationUtils.requireNonBlank(code, "Код типу продукту не може бути порожнім.");
        
        for (ProductType type : values()) {
            if (type.code.equalsIgnoreCase(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Невідомий тип продукту: " + code);
    }
}

