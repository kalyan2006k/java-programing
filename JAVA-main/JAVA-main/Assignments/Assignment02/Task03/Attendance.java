import java.util.*;

class Student {
    private String regNo;
    private final String name;

    Student(String regNo, String name) { this.regNo = regNo; this.name = name; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student s = (Student) o;
        return Objects.equals(regNo, s.regNo);
    }

    // Correct implementation: equal registration numbers produce equal hash codes.
    @Override public int hashCode() { return Objects.hash(regNo); }

    void changeRegNo(String newRegNo) { regNo = newRegNo; }
    @Override public String toString() { return regNo + " " + name; }
}

public class Attendance {
    static class BrokenStudent {
        final String regNo;
        BrokenStudent(String regNo) { this.regNo = regNo; }
        @Override public boolean equals(Object o) {
            return o instanceof BrokenStudent && regNo.equals(((BrokenStudent)o).regNo);
        }
        // Deliberately missing hashCode() for the contract demonstration.
    }

    public static void main(String[] args) {
        System.out.println("Test 1: same object reference inserted twice");
        Set<BrokenStudent> sameReference = new HashSet<>();
        BrokenStudent one = new BrokenStudent("192311001");
        sameReference.add(one); sameReference.add(one);
        System.out.println("Predicted size=1, observed size=" + sameReference.size());

        System.out.println("\nTest 2: distinct objects with equal IDs (broken class)");
        Set<BrokenStudent> broken = new HashSet<>();
        broken.add(new BrokenStudent("192311001"));
        broken.add(new BrokenStudent("192311001"));
        System.out.println("Expected logically unique count=1; observed size=" + broken.size()
                + " (often 2 because inherited identity hash codes differ; hash collisions can affect observation)");

        System.out.println("\nTest 3: corrected class");
        Set<Student> fixed = new HashSet<>();
        fixed.add(new Student("192311001", "Arun"));
        fixed.add(new Student("192311002", "Divya"));
        fixed.add(new Student("192311001", "Arun"));
        System.out.println("Unique students: " + fixed.size());

        System.out.println("\nMutable-key warning:");
        Student mutable = new Student("A1", "Mira");
        Set<Student> set = new HashSet<>(); set.add(mutable);
        mutable.changeRegNo("B2");
        System.out.println("After changing a hash field, contains(same object) may be false: " + set.contains(mutable));
        System.out.println("Do not mutate fields used by equals/hashCode while an object is in a hash collection.");
    }
}
