package com.bosch.coding.service;

import com.bosch.coding.entity.Inventory;
import com.bosch.coding.exceptions.InventoryException;
import com.bosch.coding.repository.InventoryRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    private final InventoryRepository inventoryRepository = mock(InventoryRepository.class);
    private final InventoryService inventoryService = new InventoryService(inventoryRepository);

    @Test
    void testUpdateInventoryAdd() {
        String fruit = "apple";
        when(inventoryRepository.findById(fruit))
                .thenReturn(Optional.of(new Inventory(fruit, 5)));

        inventoryService.updateInventory(fruit, 10, "add");

        verify(inventoryRepository, times(1))
                .save(new Inventory(fruit, 15));
    }

    @Test
    void testUpdateInventoryRemove() {
        String fruit = "apple";
        when(inventoryRepository.findById(fruit))
                .thenReturn(Optional.of(new Inventory(fruit, 10)));

        inventoryService.updateInventory(fruit, 5, "remove");

        verify(inventoryRepository, times(1))
                .save(new Inventory(fruit, 5));
    }

    @Test
    void testUpdateInventoryNewFruit() {
        String fruit = "orange";
        when(inventoryRepository.findById(fruit)).thenReturn(Optional.empty());

        inventoryService.updateInventory(fruit, 15, "add");

        verify(inventoryRepository, times(1))
                .save(new Inventory(fruit, 15));
    }

    @Test
    void testUpdateInventoryNegativeQuantity() {
        String fruit = "apple";
        when(inventoryRepository.findById(fruit))
                .thenReturn(Optional.of(new Inventory(fruit, 10)));

        assertThrows(InventoryException.class,
                () -> inventoryService.updateInventory(fruit, 15, "remove"));
    }

    @Test
    void testUpdateInventoryInvalidCommand() {
        assertThrows(InventoryException.class,
                () -> inventoryService.updateInventory("apple", 10, "delete"));
    }

    @Test
    void testUpdateInventoryZeroQuantity() {
        assertThrows(InventoryException.class,
                () -> inventoryService.updateInventory("apple", 0, "add"));
    }
}