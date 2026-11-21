package parking.model;

public class ParkingSlot {
    private final int floorNumber;
    private final int slotNumber;
    private Vehicle vehicle;

    public ParkingSlot(int floorNumber, int slotNumber) {
        if (floorNumber < 0 || slotNumber < 0)
            throw new IllegalArgumentException("Floor and slot numbers must be non-negative");
        this.floorNumber = floorNumber;
        this.slotNumber = slotNumber;
    }

    public boolean isFree() { return vehicle == null; }
    public void park(Vehicle vehicle) {
        if (vehicle == null) throw new IllegalArgumentException("Vehicle is required");
        if (!isFree()) throw new IllegalStateException("Slot is occupied");
        this.vehicle = vehicle;
    }
    public void release() {
        if (isFree()) throw new IllegalStateException("Slot is already free");
        vehicle = null;
    }
    public int getFloorNumber() { return floorNumber; }
    public int getSlotNumber() { return slotNumber; }
    public Vehicle getVehicle() { return vehicle; }
}
