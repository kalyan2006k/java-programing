import java.time.LocalDateTime;

class ParkingTicket {
    private static long nextTicketNumber = 1000;
    private final long ticketNumber;
    private final String registrationNumber;
    private final LocalDateTime entryTime;

    // Default constructor delegates to the parameterised constructor.
    public ParkingTicket() {
        this("UNKNOWN", LocalDateTime.now());
    }

    public ParkingTicket(String registrationNumber) {
        this(registrationNumber, LocalDateTime.now());
    }

    public ParkingTicket(String registrationNumber, LocalDateTime entryTime) {
        if (registrationNumber == null || registrationNumber.isBlank())
            throw new IllegalArgumentException("Registration number is required");
        if (entryTime == null) throw new IllegalArgumentException("Entry time is required");
        this.ticketNumber = ++nextTicketNumber;
        this.registrationNumber = registrationNumber;
        this.entryTime = entryTime;
    }

    // Copy-style constructor creates a new ticket number for the same vehicle/time.
    public ParkingTicket(ParkingTicket other) {
        this(other.registrationNumber, other.entryTime);
    }

    public String toString() {
        return "Ticket " + ticketNumber + " | " + registrationNumber + " | " + entryTime;
    }
}

public class ParkingTicketDemo {
    public static void main(String[] args) {
        ParkingTicket first = new ParkingTicket("TN01AB1111");
        ParkingTicket second = new ParkingTicket("TN02CD2222", LocalDateTime.of(2026, 11, 19, 9, 0));
        ParkingTicket copy = new ParkingTicket(first);

        System.out.println(first);
        System.out.println(second);
        System.out.println(copy);
    }
}
