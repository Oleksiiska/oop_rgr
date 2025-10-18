package core.domain.shop;

public class ProductFactory {

    public Product createProduct(String type, String name, double price, String... params) {
        if ("CLOSE".equalsIgnoreCase(type)) {
            return new Clothes(name, price, params[0], params[1]);
        } else if ("SUPPLEMENT".equalsIgnoreCase(type)) {
            return new Supplement(name, price, params[0]);
        }
        throw new IllegalArgumentException("Невідомий тип продукту: " + type);
    }
}