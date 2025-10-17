package src.core.domain.shop;

public class Supplement extends Product {
    private String flavor;

    public Supplement(String name, double price, String flavor) {
        super(name, price);
        this.flavor = flavor;
    }

    @Override
    public String getDetails() {
        return "Добавка: " + name + ", Смак: " + flavor;
    }

    public String getFlavor() {
        return flavor;
    }
}