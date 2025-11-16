package core.domain.shop;

@FunctionalInterface
public interface ProductCreator {
    Product create(String name, double price, String... params);
}

