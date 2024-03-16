package com.example.test3;

import java.io.Serializable;

public class ProductCar implements Serializable {
    private String documentId;
    private String licensePlate;
    private String brand;
    private String model;
    private int seats;
    private String transmission;
    private String fuelType;
    private String imageUrl;

    public ProductCar() {
        // Empty constructor needed for Firestore
    }

    public ProductCar(String documentId, String licensePlate, String brand, String model, int seats, String transmission, String fuelType, String imageUrl) {
        this.documentId = documentId;
        this.licensePlate = licensePlate;
        this.brand = brand;
        this.model = model;
        this.seats = seats;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.imageUrl = imageUrl;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
