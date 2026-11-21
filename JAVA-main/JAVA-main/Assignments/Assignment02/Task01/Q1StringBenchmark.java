import java.util.*;

public class Q1StringBenchmark {
    static final int WARMUPS = 2, RUNS = 5;

    interface Builder { String build(int tickets, int fields); }

    static String usingPlus(int tickets, int fields) {
        String last = "";
        for (int t = 0; t < tickets; t++) {
            String s = "";
            for (int f = 0; f < fields; f++) s += "F" + f + "=" + (t + f) + ";";
            last = s;
        }
        return last;
    }

    static String usingStringBuilder(int tickets, int fields) {
        String last = "";
        for (int t = 0; t < tickets; t++) {
            StringBuilder s = new StringBuilder();
            for (int f = 0; f < fields; f++) s.append("F").append(f).append('=').append(t + f).append(';');
            last = s.toString();
        }
        return last;
    }

    static String usingStringBuffer(int tickets, int fields) {
        String last = "";
        for (int t = 0; t < tickets; t++) {
            StringBuffer s = new StringBuffer();
            for (int f = 0; f < fields; f++) s.append("F").append(f).append('=').append(t + f).append(';');
            last = s.toString();
        }
        return last;
    }

    static long measure(Builder b, int tickets, int fields) {
        long total = 0;
        for (int i = 0; i < WARMUPS; i++) b.build(tickets, fields);
        for (int i = 0; i < RUNS; i++) {
            long start = System.nanoTime();
            b.build(tickets, fields);
            total += System.nanoTime() - start;
        }
        return total / RUNS;
    }

    static long usedMemory() {
        Runtime r = Runtime.getRuntime();
        return r.totalMemory() - r.freeMemory();
    }

    public static void main(String[] args) {
        int[][] workloads = {{1, 6}, {10000, 50}, {2000, 200}};
        String[] names = {"Short ticket", "10,000 tickets x 50 fields", "2,000 tickets x 200 fields"};
        Builder[] builders = {Q1StringBenchmark::usingPlus, Q1StringBenchmark::usingStringBuilder,
                              Q1StringBenchmark::usingStringBuffer};

        for (int w = 0; w < workloads.length; w++) {
            int tickets = workloads[w][0], fields = workloads[w][1];
            System.out.println("\n" + names[w]);
            for (int a = 0; a < builders.length; a++) {
                System.gc();
                long before = usedMemory();
                long ns = measure(builders[a], tickets, fields);
                long after = usedMemory();
                System.out.printf("%-14s avg=%d ns, heap delta approx=%d bytes%n",
                        new String[]{"String +", "StringBuilder", "StringBuffer"}[a], ns, after - before);
            }
        }
        System.out.println("\nIntermediate-object estimate: repeated + creates a new String for each concatenation step;");
        System.out.println("builder/buffer use a mutable buffer and create the final String per ticket.");
        System.out.println("Recommendation: StringBuilder for a ticket local to one thread; synchronized StringBuffer");
        System.out.println("only if multiple threads truly share the same mutable buffer. Prefer a thread-safe logger for shared logs.");
    }
}
