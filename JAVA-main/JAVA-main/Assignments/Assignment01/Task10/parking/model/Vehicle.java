package parking.model;

public class Vehicle {
    private final String registrationNumber;
    private final String type;

    public Vehicle(String registrationNumber, String type) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        if (type == null || type.isBlank())
            throw new IllegalArgumentException("Vehicle type is required");
        this.registrationNumber = registrationNumber.trim().toUpperCase();
        this.type = type.trim();
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public String getType() { return type; }
}
