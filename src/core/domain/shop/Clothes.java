package core.domain.shop;

public class Clothes extends Product {
    private final String size;
    private final String color;

    public Clothes(String name, double price, String size, String color) {
        super(name, price);
        this.size = size;
        this.color = color;
    }

    @Override
    public String getDetails() {
        return "Одяг: " + name + ", Розмір: " + size + ", Колір: " + color;
    }

    public String getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }
}