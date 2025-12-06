package core.domain.shop;

import core.exceptions.ProductOutOfStockException;
import core.event.Event;
import core.event.Observer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

    @Test
    void removeProductEmitsLowInventoryEvent() throws ProductOutOfStockException {
        Inventory inventory = new Inventory();
        ProductFactory factory = new ProductFactory();
        Product product = factory.createProduct("CLOSE", "Килимок для йоги", 800, "M", "Blue");
        TestObserver observer = new TestObserver();
        inventory.addObserver(observer);
        inventory.addProduct(product, 10);

        inventory.removeProduct(product, 6);

        assertEquals(4, inventory.getStockLevel(product));
        Event event = observer.getLastEvent();
        assertNotNull(event, "Low stock event should be emitted when threshold reached");
        assertEquals(Event.EventType.LOW_INVENTORY, event.getType());
        assertTrue(event.getMessage().contains(product.getName()));
    }

    @Test
    void removeProductThrowsWhenStockInsufficient() {
        Inventory inventory = new Inventory();
        ProductFactory factory = new ProductFactory();
        Product product = factory.createProduct("CLOSE", "Килимок для йоги", 800, "M", "Blue");
        inventory.addProduct(product, 2);

        assertThrows(ProductOutOfStockException.class, () -> inventory.removeProduct(product, 5));
    }

    private static final class TestObserver implements Observer<Event> {
        private Event lastEvent;

        @Override
        public void update(Event event) {
            this.lastEvent = event;
        }

        Event getLastEvent() {
            return lastEvent;
        }
    }
}

