package core.domain.shop;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductFactoryTest {
    private ProductFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ProductFactory();
    }

    @Test
    void testCreateClothesProduct() {
        Product product = factory.createProduct("CLOSE", "Футболка", 500, "M", "Blue");
        
        assertNotNull(product);
        assertTrue(product instanceof Clothes);
        assertEquals("Футболка", product.getName());
        assertEquals(500, product.getPrice());
        
        Clothes clothes = (Clothes) product;
        assertEquals("M", clothes.getSize());
        assertEquals("Blue", clothes.getColor());
    }

    @Test
    void testCreateSupplementProduct() {
        Product product = factory.createProduct("SUPPLEMENT", "Протеїн", 1200, "Шоколад");
        
        assertNotNull(product);
        assertTrue(product instanceof Supplement);
        assertEquals("Протеїн", product.getName());
        assertEquals(1200, product.getPrice());
        
        Supplement supplement = (Supplement) product;
        assertEquals("Шоколад", supplement.getFlavor());
    }

    @Test
    void testCreateProductWithProductType() {
        Product product = factory.createProduct(ProductType.CLOTHES, "Штани", 800, "L", "Чорний");
        
        assertNotNull(product);
        assertTrue(product instanceof Clothes);
    }

    @Test
    void testCreateClothesThrowsWhenMissingParams() {
        assertThrows(IllegalArgumentException.class, () -> 
            factory.createProduct("CLOSE", "Футболка", 500, "M"));
    }

    @Test
    void testCreateSupplementThrowsWhenMissingParams() {
        assertThrows(IllegalArgumentException.class, () -> 
            factory.createProduct("SUPPLEMENT", "Протеїн", 1200));
    }

    @Test
    void testCreateProductThrowsWhenInvalidTypeCode() {
        assertThrows(IllegalArgumentException.class, () -> 
            factory.createProduct("INVALID", "Product", 100));
    }

    @Test
    void testCreateProductThrowsWhenTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            factory.createProduct((ProductType) null, "Product", 100));
    }

    @Test
    void testRegisterNewProductType() {
        ProductFactory.register(ProductType.CLOTHES, (name, price, params) -> {
            if (params.length < 2) {
                throw new IllegalArgumentException("Need size and color");
            }
            return new Clothes(name, price, params[0], params[1]);
        });
        
        // Should still work after re-registering
        Product product = factory.createProduct("CLOSE", "Test", 100, "M", "Red");
        assertNotNull(product);
    }

    @Test
    void testRegisterThrowsWhenTypeNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ProductFactory.register(null, (name, price, params) -> null));
    }

    @Test
    void testRegisterThrowsWhenCreatorNull() {
        assertThrows(IllegalArgumentException.class, () -> 
            ProductFactory.register(ProductType.CLOTHES, null));
    }
}

