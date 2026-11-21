import java.util.*;

class Patient {
    final String name;
    final int severity;
    final long arrival;
    Patient(String name, int severity, long arrival) {
        this.name = name; this.severity = severity; this.arrival = arrival;
    }
    @Override public String toString() { return name + "(S" + severity + ")"; }
}

public class Triage {
    private long counter = 0;
    private final PriorityQueue<Patient> queue = new PriorityQueue<>(
            Comparator.comparingInt((Patient p) -> p.severity)
                    .thenComparingLong(p -> p.arrival));

    void admit(String name, int severity) {
        if (severity < 1 || severity > 5) throw new IllegalArgumentException("Severity must be 1..5");
        queue.add(new Patient(name, severity, counter++));
    }
    Patient next() { return queue.poll(); }

    public static void main(String[] args) {
        Triage er = new Triage();
        er.admit("Ravi", 3); er.admit("Meena", 1); er.admit("John", 3);
        er.admit("Asha", 5); er.admit("Kumar", 1); er.admit("Lata", 2);
        StringBuilder order = new StringBuilder();
        Patient p;
        while ((p = er.next()) != null) order.append(p).append(' ');
        System.out.println(order.toString().trim());

        System.out.println("\nComplexity: PriorityQueue add=poll=O(log n), peek=O(1), space=O(n).");
        System.out.println("The arrival sequence breaks ties FIFO for equal severity.");
        System.out.println("A direct PriorityQueue print is not sorted; repeatedly poll a copy to display treatment order.");
        System.out.println("Fairness option: increase priority after a maximum waiting-time threshold (aging),");
        System.out.println("or reserve service capacity for lower-severity queues to reduce starvation.");

        for (int n : new int[]{1000, 10000, 50000}) {
            PriorityQueue<Integer> q = new PriorityQueue<>();
            long start = System.nanoTime();
            for (int i = n; i > 0; i--) q.add(i);
            long add = System.nanoTime() - start;
            start = System.nanoTime();
            while (!q.isEmpty()) q.poll();
            long remove = System.nanoTime() - start;
            System.out.printf("n=%d: add-all=%d ns, remove-all=%d ns%n", n, add, remove);
        }
    }
}
