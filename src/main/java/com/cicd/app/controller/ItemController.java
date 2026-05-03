package com.cicd.app.controller;

import com.cicd.app.model.Item;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@GetMapping("/")
public ResponseEntity<String> home() {
    return ResponseEntity.ok("CI/CD Pipeline App is running! Version: 1.0.0");
}
@RequestMapping("/api")
public class ItemController {

    private final List<Item> items = new ArrayList<>(List.of(
        new Item(1L, "Item One",   "demo", "2024-01-01T00:00:00Z"),
        new Item(2L, "Item Two",   "demo", "2024-01-01T00:00:00Z"),
        new Item(3L, "Item Three", "demo", "2024-01-01T00:00:00Z")
    ));

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of(
            "status",  "healthy",
            "service", "cicd-app",
            "version", "1.0.0"
        ));
    }

    @GetMapping("/items")
    public ResponseEntity<Map<String, Object>> getAllItems() {
        return ResponseEntity.ok(Map.of(
            "items", items,
            "count", items.size()
        ));
    }

    @GetMapping("/items/{id}")
    public ResponseEntity<?> getItemById(@PathVariable Long id) {
        return items.stream()
            .filter(item -> item.getId().equals(id))
            .findFirst()
            .<ResponseEntity<?>>map(ResponseEntity::ok)
            .orElse(ResponseEntity
                .status(404)
                .body(Map.of("error", "item not found", "id", id)));
    }

    @PostMapping("/items")
    public ResponseEntity<?> createItem(@RequestBody Item item) {
        if (item.getName() == null || item.getName().isBlank()) {
            return ResponseEntity
                .status(400)
                .body(Map.of("error", "name is required"));
        }
        Item newItem = new Item(
            System.currentTimeMillis(),
            item.getName().trim(),
            item.getCategory() != null ? item.getCategory() : "general",
            Instant.now().toString()
        );
        items.add(newItem);
        return ResponseEntity.status(201).body(newItem);
    }

    @PutMapping("/items/{id}")
    public ResponseEntity<?> updateItem(
            @PathVariable Long id,
            @RequestBody Item updatedItem) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId().equals(id)) {
                Item existing = items.get(i);
                existing.setName(
                    updatedItem.getName() != null
                        ? updatedItem.getName()
                        : existing.getName()
                );
                existing.setCategory(
                    updatedItem.getCategory() != null
                        ? updatedItem.getCategory()
                        : existing.getCategory()
                );
                items.set(i, existing);
                return ResponseEntity.ok(existing);
            }
        }
        return ResponseEntity
            .status(404)
            .body(Map.of("error", "item not found"));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        boolean removed = items.removeIf(item -> item.getId().equals(id));
        if (!removed) {
            return ResponseEntity
                .status(404)
                .body(Map.of("error", "item not found"));
        }
        return ResponseEntity.ok(Map.of(
            "message", "item " + id + " deleted successfully"
        ));
    }
}
