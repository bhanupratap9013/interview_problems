package com.bosch.coding.utility;

import com.bosch.coding.entity.Inventory;
import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class EventGenerator {

    // List of fruits and commands to generate random events
    private static final String[] FRUITS = {"apples", "bananas", "oranges", "pears"};
    private static final String[] COMMANDS = {"add", "remove"};
    private final Random random;

    public EventGenerator() {
        this.random = new Random(); // Initialize the random number generator
    }

    // Generate a random inventory event
    public Inventory generateEvent() {
        String fruit = FRUITS[random.nextInt(FRUITS.length)]; // Randomly pick a fruit
        int quantity = random.nextInt(10) + 1; // Random quantity between 1 and 10
        String command = COMMANDS[random.nextInt(COMMANDS.length)]; // Randomly pick add/remove command

        return new Inventory(fruit, quantity, command); // Return the generated event
    }
}