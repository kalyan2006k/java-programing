import java.util.*;

public class Q2ListBenchmark {
    static volatile long sink;
    static long time(Runnable r) {
        long s = System.nanoTime(); r.run(); return System.nanoTime() - s;
    }

    static void test(int n) {
        System.out.println("\nList size = " + n);
        for (String type : new String[]{"ArrayList", "LinkedList"}) {
            List<Integer> list = type.equals("ArrayList") ? new ArrayList<>() : new LinkedList<>();
            long addEnd = time(() -> { for (int i = 0; i < n; i++) list.add(i); });
            long frontInsert = time(() -> { for (int i = 0; i < Math.min(n, 3000); i++) list.add(0, -i); });
            long middleInsert = time(() -> { for (int i = 0; i < Math.min(n, 1000); i++) list.add(list.size()/2, i); });
            int k = list.size() / 2;
            long get = time(() -> { long x = 0; for (int i = 0; i < 10000; i++) x += list.get(k); sink = x; });
            long iterate = time(() -> { long x = 0; for (int v : list) x += v; sink = x; });
            long frontRemove = time(() -> { int count = Math.min(1000, list.size()); for (int i = 0; i < count; i++) list.remove(0); });
            System.out.printf("%s: addEnd=%d ns, frontInsert(up to 3000)=%d ns, middleInsert(up to 1000)=%d ns, get(k)x10000=%d ns, iterate=%d ns, frontRemove(up to 1000)=%d ns%n",
                    type, addEnd, frontInsert, middleInsert, get, iterate, frontRemove);
        }
    }

    public static void main(String[] args) {
        test(1000); test(10000); test(30000);
        ArrayDeque<Integer> upNext = new ArrayDeque<>();
        upNext.addFirst(2); upNext.addLast(3); upNext.addFirst(1);
        System.out.println("\nArrayDeque Up Next (front to back): " + upNext);
        upNext.removeFirst();
        System.out.println("After serving front: " + upNext);
        System.out.println("\nInterpretation: ArrayList suits frequent indexed access, iteration and end-appends.");
        System.out.println("LinkedList avoids shifting for end operations but indexed access is linear.");
        System.out.println("ArrayDeque is a strong choice for frequent additions/removals at both ends; it has no get(k).");
    }
}
