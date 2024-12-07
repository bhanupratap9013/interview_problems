package com.bosch.coding.service;

import com.bosch.coding.entity.Inventory;
import com.bosch.coding.exceptions.InventoryException;
import com.bosch.coding.repository.InventoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class InventoryService {

    private static final Logger logger = LoggerFactory.getLogger(InventoryService.class);
    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void updateInventory(String fruit, int quantity, String command) {
        // If the quantity is not valid, throw an error
        if (quantity <= 0) {
            throw new InventoryException("Quantity must be greater than zero");
        }

        // Only allow 'add' or 'remove' commands, anything else is invalid
        if (!command.equals("add") && !command.equals("remove")) {
            throw new InventoryException("Invalid command: " + command);
        }

        // Try to find the existing inventory for the fruit, or create one if it doesn't exist
        Inventory inventory = inventoryRepository.findById(fruit).orElse(new Inventory(fruit, 0));

        // Depending on the command, either increase or decrease the quantity
        int newQuantity = command.equals("remove")
                ? inventory.getQuantity() - quantity
                : inventory.getQuantity() + quantity;

        // Don't let the quantity go negative
        if (newQuantity < 0) {
            throw new InventoryException("Inventory cannot be negative");
        }

        // Update the inventory and save the changes
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);

        // Log the update for reference
        logger.info("Inventory updated for fruit: {}, new quantity: {}", fruit, newQuantity);
    }
}