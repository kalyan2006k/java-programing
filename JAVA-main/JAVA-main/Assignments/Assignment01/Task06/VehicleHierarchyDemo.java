class Vehicle {
    protected final String registrationNumber;

    Vehicle(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        System.out.println("Vehicle constructor");
    }

    public String getRegistrationNumber() { return registrationNumber; }
}

class Car extends Vehicle {
    protected final int seats;

    Car(String registrationNumber, int seats) {
        super(registrationNumber);
        this.seats = seats;
        System.out.println("Car constructor");
    }
}

class ElectricCar extends Car {
    private final int batteryCapacityKWh;

    ElectricCar(String registrationNumber, int seats, int batteryCapacityKWh) {
        super(registrationNumber, seats);
        this.batteryCapacityKWh = batteryCapacityKWh;
        System.out.println("ElectricCar constructor");
    }

    public void display() {
        System.out.println(registrationNumber + ", seats=" + seats
                + ", battery=" + batteryCapacityKWh + " kWh");
    }
}

public class VehicleHierarchyDemo {
    public static void main(String[] args) {
        ElectricCar ev = new ElectricCar("TN03C3", 5, 60);
        ev.display();
    }
}
