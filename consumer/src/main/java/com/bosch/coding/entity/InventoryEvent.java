package com.bosch.coding.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEvent {

    private String fruit;
    private int quantity;
    private String command;  // Command used for processing, not saved to DB
}