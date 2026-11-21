import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

interface ParkingPass {
    boolean isValid(LocalDateTime time);
    String holderName();
}

class OneDayPass implements ParkingPass {
    private final String holder;
    private final LocalDateTime validDate;

    OneDayPass(String holder, LocalDateTime validDate) {
        this.holder = holder;
        this.validDate = validDate.toLocalDate().atStartOfDay();
    }
    @Override public boolean isValid(LocalDateTime time) {
        return time.toLocalDate().equals(validDate.toLocalDate());
    }
    @Override public String holderName() { return holder; }
}

abstract class Vehicle {
    private final String regNo;
    protected Vehicle(String regNo) {
        if (regNo == null || regNo.isBlank()) throw new IllegalArgumentException("Registration required");
        this.regNo = regNo.toUpperCase();
    }
    public String getRegNo() { return regNo; }
    public abstract double parkingFee(int minutes, LocalDateTime entry, LocalDateTime exit);
}

class TwoWheeler extends Vehicle {
    TwoWheeler(String reg) { super(reg); }
    @Override public double parkingFee(int minutes, LocalDateTime in, LocalDateTime out) {
        return ((minutes + 59) / 60) * 10.0;
    }
}
class Car extends Vehicle {
    Car(String reg) { super(reg); }
    @Override public double parkingFee(int minutes, LocalDateTime in, LocalDateTime out) {
        return ((minutes + 59) / 60) * 30.0;
    }
}
class ElectricCar extends Car {
    ElectricCar(String reg) { super(reg); }
}
class EBike extends Vehicle {
    EBike(String reg) { super(reg); }
    @Override public double parkingFee(int minutes, LocalDateTime in, LocalDateTime out) {
        return 20.0; // flat parking fee
    }
}
class DeliveryVan extends Vehicle {
    DeliveryVan(String reg) { super(reg); }
    @Override public double parkingFee(int minutes, LocalDateTime in, LocalDateTime out) {
        int hours = (minutes + 59) / 60;
        boolean daytime = !in.toLocalTime().isBefore(java.time.LocalTime.of(9, 0))
                && in.toLocalTime().isBefore(java.time.LocalTime.of(17, 0));
        return hours * 30.0 * (daytime ? 2.0 : 1.0);
    }
}

class SmartParkingFacility {
    private final String[][] slots;
    private final Map<String, ParkingPass> passes = new HashMap<>();
    private final Map<String, Vehicle> parked = new HashMap<>();

    SmartParkingFacility(int[] slotsPerFloor) {
        slots = new String[slotsPerFloor.length][];
        for (int f = 0; f < slotsPerFloor.length; f++) slots[f] = new String[slotsPerFloor[f]];
    }
    public void registerPass(ParkingPass pass) { passes.put(pass.holderName(), pass); }

    public boolean park(Vehicle vehicle, String passHolder, LocalDateTime time) {
        if (parked.containsKey(vehicle.getRegNo())) return false;
        for (int f = 0; f < slots.length; f++) {
            for (int s = 0; s < slots[f].length; s++) {
                if (slots[f][s] == null) {
                    slots[f][s] = vehicle.getRegNo();
                    parked.put(vehicle.getRegNo(), vehicle);
                    System.out.println(vehicle.getRegNo() + " parked at floor " + f + ", slot " + s);
                    return true;
                }
            }
        }
        return false;
    }

    public double leave(Vehicle vehicle, String passHolder, LocalDateTime in, LocalDateTime out) {
        long minutes = Math.max(0, java.time.Duration.between(in, out).toMinutes());
        ParkingPass pass = passes.get(passHolder);
        double fee = (pass != null && pass.isValid(out))
                ? 0.0 : vehicle.parkingFee((int) minutes, in, out);
        parked.remove(vehicle.getRegNo());
        for (String[] floor : slots) {
            for (int s = 0; s < floor.length; s++) {
                if (vehicle.getRegNo().equals(floor[s])) floor[s] = null;
            }
        }
        return fee;
    }
}

public class DesignChallenge {
    public static void main(String[] args) {
        // Different slot counts on each floor: jagged array.
        SmartParkingFacility facility = new SmartParkingFacility(new int[]{2, 3, 1, 4, 2});
        LocalDateTime entry = LocalDateTime.of(2026, 11, 19, 10, 0);

        facility.registerPass(new OneDayPass("Visitor-A", entry));
        Vehicle bike = new EBike("TN10EB1234");
        Vehicle van = new DeliveryVan("TN20DV5678");
        Vehicle ev = new ElectricCar("TN30EV9012");

        facility.park(bike, "", entry);
        facility.park(van, "", entry);
        facility.park(ev, "Visitor-A", entry);

        System.out.println("E-bike flat fee: Rs " + facility.leave(bike, "", entry, entry.plusHours(2)));
        System.out.println("Delivery van daytime fee: Rs " + facility.leave(van, "", entry, entry.plusHours(2)));
        System.out.println("Visitor one-day pass fee: Rs " + facility.leave(ev, "Visitor-A", entry, entry.plusHours(2)));
    }
}
