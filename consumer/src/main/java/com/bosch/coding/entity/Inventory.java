package com.bosch.coding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Inventory {

    @Id
    private String fruit;  // Use fruit as the primary key
    private int quantity;

    @Override
    public String toString() {
        return String.format("{\"fruit\":\"%s\", \"quantity\":%d}", fruit, quantity);
    }
}