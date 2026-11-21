import java.util.*;

public class Q4MapBenchmark {
    static volatile int sink;
    static long lookupTime(Map<Integer, Integer> map, int count) {
        long start = System.nanoTime();
        int sum = 0;
        for (int i = 0; i < count; i++) sum += map.get(i % map.size());
        sink = sum;
        return System.nanoTime() - start;
    }

    static void run(int n) {
        Map<Integer,Integer> hash = new HashMap<>();
        Map<Integer,Integer> tree = new TreeMap<>();
        Map<Integer,Integer> linked = new LinkedHashMap<>();
        for (int i = n - 1; i >= 0; i--) {
            hash.put(i, i * 10); tree.put(i, i * 10); linked.put(i, i * 10);
        }
        int lookups = 100_000;
        System.out.println("\nMedicines = " + n);
        System.out.printf("100,000 get(): HashMap=%d ns, TreeMap=%d ns, LinkedHashMap=%d ns%n",
                lookupTime(hash, lookups), lookupTime(tree, lookups), lookupTime(linked, lookups));
        long a = System.nanoTime(); int hashCount = 0; for (int k : hash.keySet()) hashCount += k;
        long hashIter = System.nanoTime() - a;
        a = System.nanoTime(); int treeCount = 0; for (int k : tree.keySet()) treeCount += k;
        long treeIter = System.nanoTime() - a;
        a = System.nanoTime(); int linkedCount = 0; for (int k : linked.keySet()) linkedCount += k;
        long linkedIter = System.nanoTime() - a;
        sink = hashCount + treeCount + linkedCount;
        System.out.printf("key iteration: HashMap=%d ns, TreeMap=%d ns, LinkedHashMap=%d ns%n",
                hashIter, treeIter, linkedIter);
        System.out.println("First keys: HashMap=" + first(hash, 5) + ", TreeMap=" + first(tree, 5)
                + ", LinkedHashMap=" + first(linked, 5));
        long s = System.nanoTime(); new TreeMap<>(hash); long sortedReport = System.nanoTime() - s;
        s = System.nanoTime(); new ArrayList<>(linked.keySet()); long insertionReport = System.nanoTime() - s;
        System.out.printf("Extra report preparation: sort/copy HashMap to TreeMap=%d ns; copy insertion-order keys=%d ns%n",
                sortedReport, insertionReport);
    }

    static List<Integer> first(Map<Integer,Integer> m, int limit) {
        List<Integer> out = new ArrayList<>();
        for (int k : m.keySet()) { if (out.size() == limit) break; out.add(k); }
        return out;
    }

    public static void main(String[] args) {
        run(100); run(10000); run(100000);
        System.out.println("\nChoice: HashMap for fastest average code lookup; TreeMap for code-sorted report;");
        System.out.println("LinkedHashMap for insertion-order report. Hashtable is legacy synchronized API;");
        System.out.println("prefer modern Map implementations and explicit concurrency tools when needed.");
    }
}
