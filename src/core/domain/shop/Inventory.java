package core.domain.shop;

import core.exceptions.ProductOutOfStockException;
import core.interfaces.IProduct;
import core.patterns.observer.Event;
import core.patterns.observer.Observable;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<IProduct, Integer> stock;
    private final Observable<Event> eventObservable;
    private static final int LOW_STOCK_THRESHOLD = 5;

    public Inventory() {
        this.stock = new HashMap<>();
        this.eventObservable = new Observable<>();
    }
    
    public void addObserver(core.patterns.observer.Observer<Event> observer) {
        eventObservable.addObserver(observer);
    }
    
    public void removeObserver(core.patterns.observer.Observer<Event> observer) {
        eventObservable.removeObserver(observer);
    }

    public void addProduct(IProduct product, int quantity) {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Клькість завжди додатня.");
        }
        stock.put(product, stock.getOrDefault(product, 0) + quantity);
    }

    public void removeProduct(IProduct product, int quantity) throws ProductOutOfStockException {
        if (product == null) {
            throw new IllegalArgumentException("Продукт не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Клькість завжди додатня.");
        }
        int currentStock = getStockLevel(product);
        if (currentStock < quantity) {
            throw new ProductOutOfStockException(
                    "Недостатньо товару '" + product.getName() + "' на складі. В наявності: " + currentStock + ", запитано: " + quantity
            );
        }
        int newStock = currentStock - quantity;
        stock.put(product, newStock);
        
        if (newStock <= LOW_STOCK_THRESHOLD && newStock > 0) {
            eventObservable.notifyObservers(new Event(Event.EventType.LOW_INVENTORY, 
                "Низький залишок товару '" + product.getName() + "': " + newStock + " од.", this));
        }
    }

    public boolean hasEnoughStock(IProduct product, int quantity) {
        if (product == null) {
            return false;
        }
        return getStockLevel(product) >= quantity;
    }

    public int getStockLevel(IProduct product) {
        if (product == null) {
            return 0;
        }
        return stock.getOrDefault(product, 0);
    }

    public Map<IProduct, Integer> getStock() {
        return Map.copyOf(stock);
    }
}