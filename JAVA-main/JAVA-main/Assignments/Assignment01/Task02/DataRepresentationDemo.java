import java.util.Scanner;

public class DataRepresentationDemo {
    public static void main(String[] args) {
        // Jagged array: each floor can contain a different number of slots.
        String[][] slots = new String[3][];
        slots[0] = new String[2];
        slots[1] = new String[3];
        slots[2] = new String[1];

        int floor = 1;
        int slot = 2;
        String vehicleNo = "TN02CD2222";
        slots[floor][slot] = vehicleNo;

        int parkingMinutes = 130;
        double hourlyRate = 30.0;

        // Integer ceiling division: 130 minutes -> 3 billable hours.
        int billableHours = (parkingMinutes + 59) / 60;
        double fee = billableHours * hourlyRate;

        // Demonstrate the integer division pitfall:
        int wrongHours = parkingMinutes / 60; // 2, because integer division truncates.
        System.out.println("Stored vehicle: " + slots[floor][slot]);
        System.out.println("Correct billable hours: " + billableHours);
        System.out.println("Correct fee: Rs " + fee);
        System.out.println("Incorrect integer-division hours: " + wrongHours);

        // Optional input example:
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter vehicle registration number: ");
        String input = sc.nextLine().trim();
        System.out.println("Input received: " + input);
        sc.close();
    }
}
