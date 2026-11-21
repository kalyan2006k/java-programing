package parking.service;

import parking.model.ParkingSlot;
import parking.model.Vehicle;

public class ParkingService {
    private final ParkingSlot[][] slots;

    public ParkingService(int[] slotsPerFloor) {
        slots = new ParkingSlot[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) {
            if (slotsPerFloor[f] < 0) throw new IllegalArgumentException("Negative slot count");
            slots[f] = new ParkingSlot[slotsPerFloor[f]];
            for (int s = 0; s < slotsPerFloor[f]; s++)
                slots[f][s] = new ParkingSlot(f, s);
        }
    }

    public ParkingSlot park(Vehicle vehicle) {
        for (ParkingSlot[] floor : slots) {
            for (ParkingSlot slot : floor) {
                if (slot.isFree()) {
                    slot.park(vehicle);
                    return slot;
                }
            }
        }
        return null;
    }

    public boolean release(int floor, int slot) {
        if (floor < 0 || floor >= slots.length) return false;
        if (slot < 0 || slot >= slots[floor].length) return false;
        if (slots[floor][slot].isFree()) return false;
        slots[floor][slot].release();
        return true;
    }
}
