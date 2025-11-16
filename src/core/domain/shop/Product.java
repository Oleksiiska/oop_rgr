package core.domain.shop;

import core.interfaces.IProduct;
import java.util.UUID;

public abstract class Product implements IProduct {
    protected final String id;
    protected String name;
    protected double price;

    public Product(String name, double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Ціна лише додатня.");
        }
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
    }

    public abstract String getDetails();

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
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