import java.util.ArrayList;
import java.util.List;

class Vehicle {
    private final String registrationNumber;
    private final String type;

    Vehicle(String registrationNumber, String type) {
        this.registrationNumber = registrationNumber;
        this.type = type;
    }

    public String getRegistrationNumber() { return registrationNumber; }
    public String getType() { return type; }
}

class ParkingSlot {
    private final int floor;
    private final int number;
    private Vehicle vehicle;

    ParkingSlot(int floor, int number) {
        this.floor = floor;
        this.number = number;
    }

    public boolean isFree() { return vehicle == null; }
    public void park(Vehicle vehicle) {
        if (!isFree()) throw new IllegalStateException("Slot is occupied");
        this.vehicle = vehicle;
    }
    public void release() {
        if (isFree()) throw new IllegalStateException("Slot is already free");
        vehicle = null;
    }
    public int getFloor() { return floor; }
    public int getNumber() { return number; }
}

class ParkingTicket {
    private final long ticketNumber;
    private final Vehicle vehicle;

    ParkingTicket(long ticketNumber, Vehicle vehicle) {
        this.ticketNumber = ticketNumber;
        this.vehicle = vehicle;
    }
    public long getTicketNumber() { return ticketNumber; }
    public Vehicle getVehicle() { return vehicle; }
}

class ParkingFacility {
    private final List<ParkingSlot> slots = new ArrayList<>();
    ParkingFacility(int floors, int slotsPerFloor) {
        for (int f = 0; f < floors; f++)
            for (int s = 0; s < slotsPerFloor; s++)
                slots.add(new ParkingSlot(f, s));
    }
    public int totalSlots() { return slots.size(); }
}

public class ObjectModelDemo {
    public static void main(String[] args) {
        Vehicle vehicle = new Vehicle("TN01AB1111", "Car");
        ParkingTicket ticket = new ParkingTicket(1, vehicle);
        ParkingFacility facility = new ParkingFacility(2, 10);

        System.out.println("Vehicle: " + vehicle.getRegistrationNumber());
        System.out.println("Type: " + vehicle.getType());
        System.out.println("Ticket: " + ticket.getTicketNumber());
        System.out.println("Total slots: " + facility.totalSlots());
    }
}
