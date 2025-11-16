package core.domain.shop;

import java.util.HashMap;
import java.util.Map;

public class ProductFactory {
    
    private static final Map<ProductType, ProductCreator> creators = new HashMap<>();
    
    static {
        // Register product creators
        register(ProductType.CLOTHES, (name, price, params) -> {
            if (params.length < 2) {
                throw new IllegalArgumentException("Одяг потребує розмір та колір.");
            }
            return new Clothes(name, price, params[0], params[1]);
        });
        
        register(ProductType.SUPPLEMENT, (name, price, params) -> {
            if (params.length < 1) {
                throw new IllegalArgumentException("Добавка потребує смак.");
            }
            return new Supplement(name, price, params[0]);
        });
    }
    
    public static void register(ProductType type, ProductCreator creator) {
        if (type == null || creator == null) {
            throw new IllegalArgumentException("Тип та створювач не можуть бути null.");
        }
        creators.put(type, creator);
    }
    
    public Product createProduct(String typeCode, String name, double price, String... params) {
        ProductType type = ProductType.fromCode(typeCode);
        return createProduct(type, name, price, params);
    }
    
    public Product createProduct(ProductType type, String name, double price, String... params) {
        if (type == null) {
            throw new IllegalArgumentException("Тип продукту не може бути null.");
        }
        
        ProductCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("Тип продукту '" + type + "' не зареєстровано.");
        }
        
        return creator.create(name, price, params);
    }
}