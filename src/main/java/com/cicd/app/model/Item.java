package com.cicd.app.model;

public class Item {

    private Long id;
    private String name;
    private String category;
    private String createdAt;

    public Item() {}

    public Item(Long id, String name, String category, String createdAt) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getCreatedAt() { return createdAt; }

    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setCategory(String category) { this.category = category; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

}
