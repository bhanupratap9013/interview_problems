package com.bosch.coding.utility;

import com.bosch.coding.entity.Inventory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventGeneratorTest {

    private final EventGenerator eventGenerator = new EventGenerator();

    @Test
    void testGenerateEvent() {
        Inventory event = eventGenerator.generateEvent();

        assertNotNull(event);
        assertNotNull(event.getFruit());
        assertNotNull(event.getCommand());
        assertTrue(event.getQuantity() > 0);

        assertTrue(event.getFruit().matches("apples|bananas|oranges|pears"));
        assertTrue(event.getCommand().matches("add|remove"));
    }
}