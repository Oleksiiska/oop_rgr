package src.core.domain.shop;

public class ProductFactory {

    public Product createProduct(String type, String name, double price, String... params) {
        if ("APPAREL".equalsIgnoreCase(type)) {
            return new Apparel(name, price, params[0], params[1]);
        } else if ("SUPPLEMENT".equalsIgnoreCase(type)) {
            return new Supplement(name, price, params[0]);
        }
        throw new IllegalArgumentException("Невідомий тип продукту: " + type);
    }
}