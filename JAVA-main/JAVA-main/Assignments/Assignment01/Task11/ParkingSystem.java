import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

abstract class Vehicle {
    private final String registrationNumber;
    protected Vehicle(String registrationNumber) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        this.registrationNumber = registrationNumber.trim().toUpperCase();
    }
    public String getRegistrationNumber() { return registrationNumber; }
    public abstract double hourlyRate();
}

class Bike extends Vehicle {
    Bike(String reg) { super(reg); }
    @Override public double hourlyRate() { return 10.0; }
}
class Car extends Vehicle {
    Car(String reg) { super(reg); }
    @Override public double hourlyRate() { return 30.0; }
}
class ElectricCar extends Car {
    ElectricCar(String reg) { super(reg); }
    public double chargingFee(int units) { return units * 12.0; }
}

class ParkingTicket {
    private static long counter = 5000;
    private final long ticketNumber = ++counter;
    private final Vehicle vehicle;
    private final LocalDateTime entryTime;
    private final int floor;
    private final int slot;

    ParkingTicket(Vehicle vehicle, LocalDateTime entryTime, int floor, int slot) {
        this.vehicle = vehicle;
        this.entryTime = entryTime;
        this.floor = floor;
        this.slot = slot;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public int getFloor() { return floor; }
    public int getSlot() { return slot; }
}

class ParkingManager {
    private final String[][] slots;
    private final Map<String, ParkingTicket> activeTickets = new HashMap<>();

    ParkingManager(int[] slotsPerFloor) {
        slots = new String[slotsPerFloor.length][];
        for (int i = 0; i < slotsPerFloor.length; i++)
            slots[i] = new String[slotsPerFloor[i]];
    }

    public ParkingTicket enter(Vehicle vehicle, LocalDateTime time) {
        String reg = vehicle.getRegistrationNumber();
        if (activeTickets.containsKey(reg)) {
            System.out.println("Rejected duplicate: " + reg);
            return null;
        }
        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = reg;
                    ParkingTicket ticket = new ParkingTicket(vehicle, time, f, s);
                    activeTickets.put(reg, ticket);
                    System.out.println("Entered " + reg + " at " + f + ":" + s);
                    return ticket;
                }
            }
        }
        System.out.println("Parking full for " + reg);
        return null;
    }

    public double exit(String reg, LocalDateTime exitTime) {
        ParkingTicket ticket = activeTickets.remove(reg.toUpperCase());
        if (ticket == null) {
            System.out.println("No active ticket for " + reg);
            return -1;
        }
        long minutes = Math.max(0, Duration.between(ticket.getEntryTime(), exitTime).toMinutes());
        int hours = (int) ((minutes + 59) / 60);
        double fee = hours * ticket.getVehicle().hourlyRate();
        slots[ticket.getFloor()][ticket.getSlot()] = null;
        System.out.println("Exited " + reg + " after " + minutes + " minutes. Fee = Rs " + fee);
        return fee;
    }
}

public class ParkingSystem {
    public static void main(String[] args) {
        ParkingManager system = new ParkingManager(new int[]{1, 2});
        LocalDateTime start = LocalDateTime.of(2026, 11, 19, 9, 0);

        system.enter(new Bike("TN01AB1111"), start);
        system.enter(new Car("TN02CD2222"), start);
        system.enter(new ElectricCar("TN03EF3333"), start);
        system.enter(new Car("TN02CD2222"), start); // duplicate registration test

        double total = 0;
        total += system.exit("TN01AB1111", start.plusMinutes(60));  // boundary: 1 hour
        total += system.exit("TN02CD2222", start.plusMinutes(130)); // 3 billable hours
        total += system.exit("TN03EF3333", start.plusMinutes(0));   // zero-minute stay
        System.out.println("Total collection = Rs " + total);

        system.exit("TN99ZZ9999", start.plusHours(1)); // invalid/unknown ticket
    }
}
