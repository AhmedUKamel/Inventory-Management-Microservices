package com.ahmedukamel.inventoryservice;

import com.ahmedukamel.inventoryservice.model.Inventory;
import com.ahmedukamel.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class InventoryserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryserviceApplication.class, args);
    }

//    @Bean
    public CommandLineRunner loadData(InventoryRepository inventoryRepository) {
        return args -> {
            inventoryRepository.save(Inventory.builder()
                    .skuCode("iPhone 13")
                    .quantity(100)
                    .build());

            inventoryRepository.save(Inventory.builder()
                    .skuCode("iPhone 12")
                    .quantity(0)
                    .build());
        };
    }

}
