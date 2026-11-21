interface Chargeable {
    double chargingFee(int units);

    default String chargingMessage() {
        return "EV charging service is available.";
    }

    static boolean validUnits(int units) {
        return units >= 0;
    }
}

abstract class Vehicle {
    private final String registrationNumber;

    protected Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public abstract double calculateParkingFee(int hours);
}

class PetrolCar extends Vehicle {
    PetrolCar(String registrationNumber) { super(registrationNumber); }
    @Override public double calculateParkingFee(int hours) { return hours * 30.0; }
}

class ElectricCar extends Vehicle implements Chargeable {
    ElectricCar(String registrationNumber) { super(registrationNumber); }
    @Override public double calculateParkingFee(int hours) { return hours * 30.0; }
    @Override public double chargingFee(int units) {
        if (!Chargeable.validUnits(units)) throw new IllegalArgumentException("Invalid units");
        return units * 12.0;
    }
}

public class AbstractionDemo {
    public static void main(String[] args) {
        Vehicle[] vehicles = {
                new PetrolCar("TN01A1"),
                new ElectricCar("TN02B2")
        };

        for (Vehicle vehicle : vehicles) {
            System.out.println(vehicle.getRegistrationNumber()
                    + " parking fee = Rs " + vehicle.calculateParkingFee(2));
            if (vehicle instanceof Chargeable c) {
                System.out.println(c.chargingMessage());
                System.out.println("Charging fee = Rs " + c.chargingFee(5));
            }
        }
    }
}
