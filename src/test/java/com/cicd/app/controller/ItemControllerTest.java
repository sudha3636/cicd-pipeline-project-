package com.cicd.app.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Item Controller Tests")
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /api/health returns 200")
    void healthCheck_returns200() throws Exception {
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("healthy"));
    }

    @Test
    @DisplayName("GET /api/items returns list")
    void getAllItems_returns200WithList() throws Exception {
        mockMvc.perform(get("/api/items"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.items").isArray())
            .andExpect(jsonPath("$.items", hasSize(greaterThan(0))));
    }

    @Test
    @DisplayName("GET /api/items/1 returns correct item")
    void getItemById_existingId_returns200() throws Exception {
        mockMvc.perform(get("/api/items/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.name").value("Item One"));
    }

    @Test
    @DisplayName("GET /api/items/999 returns 404")
    void getItemById_nonExistingId_returns404() throws Exception {
        mockMvc.perform(get("/api/items/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("item not found"));
    }

    @Test
    @DisplayName("POST /api/items creates item")
    void createItem_validBody_returns201() throws Exception {
        String body = "{\"name\":\"Test Item\",\"category\":\"testing\"}";
        mockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Item"))
            .andExpect(jsonPath("$.id").exists());
    }

    @Test
    @DisplayName("POST /api/items without name returns 400")
    void createItem_missingName_returns400() throws Exception {
        String body = "{\"category\":\"testing\"}";
        mockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("name is required"));
    }

    @Test
    @DisplayName("POST /api/items without category uses default")
    void createItem_noCategory_usesDefault() throws Exception {
        String body = "{\"name\":\"No Category Item\"}";
        mockMvc.perform(post("/api/items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.category").value("general"));
    }

    @Test
    @DisplayName("PUT /api/items/2 updates item")
    void updateItem_existingId_returns200() throws Exception {
        String body = "{\"name\":\"Updated Name\",\"category\":\"updated\"}";
        mockMvc.perform(put("/api/items/2")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    @Test
    @DisplayName("PUT /api/items/999 returns 404")
    void updateItem_nonExistingId_returns404() throws Exception {
        String body = "{\"name\":\"Ghost Item\"}";
        mockMvc.perform(put("/api/items/999")
            .contentType(MediaType.APPLICATION_JSON)
            .content(body))
            .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/items/3 deletes item")
    void deleteItem_existingId_returns200() throws Exception {
        mockMvc.perform(delete("/api/items/3"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.message")
                .value(containsString("deleted successfully")));
    }

    @Test
    @DisplayName("DELETE /api/items/999 returns 404")
    void deleteItem_nonExistingId_returns404() throws Exception {
        mockMvc.perform(delete("/api/items/999"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("item not found"));
    }
}
