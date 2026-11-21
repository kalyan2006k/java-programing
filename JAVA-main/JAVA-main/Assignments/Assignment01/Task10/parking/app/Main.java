package parking.app;

import parking.model.ParkingSlot;
import parking.model.Vehicle;
import parking.service.ParkingService;

public class Main {
    public static void main(String[] args) {
        ParkingService service = new ParkingService(new int[]{2, 1, 3});
        ParkingSlot assigned = service.park(new Vehicle("TN01AB1111", "Car"));

        if (assigned != null) {
            System.out.println("Vehicle parked at floor " + assigned.getFloorNumber()
                    + ", slot " + assigned.getSlotNumber());
        }
        System.out.println("Released: " + service.release(0, 0));
        System.out.println("Release again: " + service.release(0, 0));
    }
}
