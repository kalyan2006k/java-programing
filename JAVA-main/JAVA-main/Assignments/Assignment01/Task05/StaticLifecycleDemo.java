class ParkingTicket {
    private static long ticketCounter = 0;
    private final long ticketNumber;
    private final String registrationNumber;

    ParkingTicket(String registrationNumber) {
        this.registrationNumber = registrationNumber;
        this.ticketNumber = ++ticketCounter;
    }

    public static long getTicketsIssued() { return ticketCounter; }
    public long getTicketNumber() { return ticketNumber; }
    public String getRegistrationNumber() { return registrationNumber; }
}

class ParkingFacility {
    private int currentOccupancy;

    public void vehicleEntered() { currentOccupancy++; }
    public void vehicleLeft() {
        if (currentOccupancy > 0) currentOccupancy--;
    }
    public int getCurrentOccupancy() { return currentOccupancy; }
}

public class StaticLifecycleDemo {
    public static void main(String[] args) {
        ParkingFacility north = new ParkingFacility();
        ParkingFacility south = new ParkingFacility();

        ParkingTicket t1 = new ParkingTicket("TN01AB1111");
        ParkingTicket t2 = new ParkingTicket("TN02CD2222");
        north.vehicleEntered();
        south.vehicleEntered();

        System.out.println("Ticket numbers: " + t1.getTicketNumber() + ", " + t2.getTicketNumber());
        System.out.println("Tickets issued across the application: " + ParkingTicket.getTicketsIssued());
        System.out.println("North occupancy: " + north.getCurrentOccupancy());
        System.out.println("South occupancy: " + south.getCurrentOccupancy());

        // After a vehicle exits, remove references to its ticket when it is no longer needed.
        t1 = null; // The object may become eligible for garbage collection.
        System.out.println("A ticket reference was released.");
    }
}
