package in.shareaplate.model;

import java.time.LocalDateTime;

public class Donation {
    private long id;
    private String foodName;
    private String foodType;
    private int quantity;
    private LocalDateTime availableUntil;
    private String donorName;
    private String contactDetails;
    private String pickupLocation;
    private String notes;
    private String status;

    public Donation() {}

    public Donation(long id, String foodName, String foodType, int quantity,
                    LocalDateTime availableUntil, String donorName, String contactDetails,
                    String pickupLocation, String notes, String status) {
        this.id = id; this.foodName = foodName; this.foodType = foodType; this.quantity = quantity;
        this.availableUntil = availableUntil; this.donorName = donorName; this.contactDetails = contactDetails;
        this.pickupLocation = pickupLocation; this.notes = notes; this.status = status;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public LocalDateTime getAvailableUntil() { return availableUntil; }
    public void setAvailableUntil(LocalDateTime availableUntil) { this.availableUntil = availableUntil; }
    public String getDonorName() { return donorName; }
    public void setDonorName(String donorName) { this.donorName = donorName; }
    public String getContactDetails() { return contactDetails; }
    public void setContactDetails(String contactDetails) { this.contactDetails = contactDetails; }
    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
