public class Q7StringRedesign {
    static volatile String result;

    // Original: repeated immutable String concatenation.
    static String original(int tickets, int fields) {
        String last = "";
        for (int t = 0; t < tickets; t++) {
            String ticket = "";
            for (int f = 0; f < fields; f++) ticket += "field" + f + "=" + (t + f) + ";";
            last = ticket;
        }
        return last;
    }

    // Redesigned: one mutable builder per ticket; convert once to String.
    static String redesigned(int tickets, int fields) {
        String last = "";
        for (int t = 0; t < tickets; t++) {
            StringBuilder ticket = new StringBuilder(fields * 16);
            for (int f = 0; f < fields; f++)
                ticket.append("field").append(f).append('=').append(t + f).append(';');
            last = ticket.toString();
        }
        return last;
    }

    static long benchmark(boolean useBuilder, int tickets, int fields) {
        for (int i = 0; i < 2; i++) result = useBuilder ? redesigned(tickets, fields) : original(tickets, fields);
        long total = 0;
        for (int i = 0; i < 5; i++) {
            long start = System.nanoTime();
            result = useBuilder ? redesigned(tickets, fields) : original(tickets, fields);
            total += System.nanoTime() - start;
        }
        return total / 5;
    }

    public static void main(String[] args) {
        int tickets = 5000, fields = 100;
        System.out.println("Q7 redesign validation: same workload for both implementations");
        long oldTime = benchmark(false, tickets, fields);
        long newTime = benchmark(true, tickets, fields);
        System.out.println("Original repeated + average: " + oldTime + " ns");
        System.out.println("Redesigned StringBuilder average: " + newTime + " ns");
        System.out.println("Compare your own repeated runs. Expected: Builder reduces repeated copying and temporary Strings.");
        System.out.println("Trade-off: Builder has mutable local state and a backing array; it is not shared between threads.");
        System.out.println("Complexity: repeated + can copy O(L^2) characters for a final ticket of length L;");
        System.out.println("StringBuilder append is amortized O(L) total, plus O(L) for toString().");
    }
}
