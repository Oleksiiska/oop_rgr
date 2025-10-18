package core.domain.shop;

public record OrderItem(Product product, int quantity) {

    public double getTotalPrice() {
        return product.getPrice() * quantity;
    }
}