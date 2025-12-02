package core.domain.shop;

import core.util.Constants;
import core.util.ValidationUtils;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Factory class for creating products of different types.
 * Uses the Factory pattern with registered creators for each product type.
 */
@Component
public class ProductFactory {
    
    private static final Map<ProductType, ProductCreator> creators = new HashMap<>();
    
    static {
        // Register product creators
        register(ProductType.CLOTHES, (name, price, params) -> {
            if (params.length < 2) {
                throw new IllegalArgumentException(Constants.ERROR_CLOTHES_PARAMS_INSUFFICIENT);
            }
            return new Clothes(name, price, params[0], params[1]);
        });
        
        register(ProductType.SUPPLEMENT, (name, price, params) -> {
            if (params.length < 1) {
                throw new IllegalArgumentException(Constants.ERROR_SUPPLEMENT_PARAMS_INSUFFICIENT);
            }
            return new Supplement(name, price, params[0]);
        });
    }
    
    /**
     * Registers a product creator for a specific product type.
     *
     * @param type the product type (must not be null)
     * @param creator the creator function (must not be null)
     * @throws IllegalArgumentException if type or creator is null
     */
    public static void register(ProductType type, ProductCreator creator) {
        ValidationUtils.requireNonNull(type, Constants.ERROR_FACTORY_TYPE_NULL);
        ValidationUtils.requireNonNull(creator, Constants.ERROR_FACTORY_TYPE_NULL);
        creators.put(type, creator);
    }
    
    /**
     * Creates a product from a type code string.
     *
     * @param typeCode the product type code
     * @param name the product name
     * @param price the product price
     * @param params additional parameters specific to the product type
     * @return a new Product instance
     * @throws IllegalArgumentException if type is invalid or parameters are insufficient
     */
    public Product createProduct(String typeCode, String name, double price, String... params) {
        ProductType type = ProductType.fromCode(typeCode);
        return createProduct(type, name, price, params);
    }
    
    /**
     * Creates a product of the specified type.
     *
     * @param type the product type (must not be null)
     * @param name the product name
     * @param price the product price
     * @param params additional parameters specific to the product type
     * @return a new Product instance
     * @throws IllegalArgumentException if type is null, not registered, or parameters are insufficient
     */
    public Product createProduct(ProductType type, String name, double price, String... params) {
        ValidationUtils.requireNonNull(type, Constants.ERROR_PRODUCT_TYPE_NULL);
        
        ProductCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException(String.format(Constants.ERROR_PRODUCT_TYPE_NOT_REGISTERED, type));
        }
        
        return creator.create(name, price, params);
    }
}