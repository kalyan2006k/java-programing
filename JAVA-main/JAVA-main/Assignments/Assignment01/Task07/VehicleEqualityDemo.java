import java.util.Objects;

class Vehicle {
    private final String registrationNumber;
    private final String type;

    Vehicle(String registrationNumber, String type) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        this.registrationNumber = registrationNumber.toUpperCase();
        this.type = type;
    }

    @Override
    public String toString() {
        return type + " [" + registrationNumber + "]";
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof Vehicle)) return false;
        Vehicle other = (Vehicle) object;
        return registrationNumber.equals(other.registrationNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(registrationNumber);
    }
}

public class VehicleEqualityDemo {
    public static void main(String[] args) {
        final double carRate = 30.0; // final variable
        Vehicle car1 = new Vehicle("tn02b2", "Car");
        Vehicle car2 = new Vehicle("TN02B2", "ElectricCar");

        System.out.println(car1);
        System.out.println("Same registration? " + car1.equals(car2));
        System.out.println("Hash codes equal? " + (car1.hashCode() == car2.hashCode()));
        System.out.println("Car rate: " + carRate);
    }
}
