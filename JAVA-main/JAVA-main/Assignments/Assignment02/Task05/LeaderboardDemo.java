import java.util.*;

class Team {
    final String name;
    final int points;
    final int goalDiff;

    Team(String name, int points, int goalDiff) {
        this.name = name; this.points = points; this.goalDiff = goalDiff;
    }
    @Override public String toString() { return name + "(" + points + "," + goalDiff + ")"; }
}

class Leaderboard<T> {
    private final List<T> items = new ArrayList<>();
    private final Comparator<? super T> order;
    Leaderboard(Comparator<? super T> order) { this.order = order; }
    void add(T item) { items.add(item); }
    List<T> ranking() {
        List<T> copy = new ArrayList<>(items);
        copy.sort(order);
        return copy;
    }
}

public class LeaderboardDemo {
    public static void main(String[] args) {
        Comparator<Team> byRank = Comparator.comparingInt((Team t) -> t.points).reversed()
                .thenComparing(Comparator.comparingInt((Team t) -> t.goalDiff).reversed())
                .thenComparing(t -> t.name);

        Leaderboard<Team> board = new Leaderboard<>(byRank);
        board.add(new Team("Panthers", 9, 4));
        board.add(new Team("Eagles", 9, 6));
        board.add(new Team("Cobras", 6, 1));
        board.add(new Team("Bisons", 9, 4));
        System.out.println(board.ranking());

        List<Team> ties = Arrays.asList(new Team("Zeta", 5, 2), new Team("Alpha", 5, 2),
                new Team("Beta", 5, 3), new Team("Delta", 7, 0));
        ties.sort(byRank);
        System.out.println("Tie-break test: " + ties);

        Comparator<Team> faulty = (a, b) -> b.points - a.points; // subtraction can overflow; ignores tie-breakers
        List<Team> faultyTest = new ArrayList<>(Arrays.asList(new Team("A", Integer.MIN_VALUE, 0),
                new Team("B", Integer.MAX_VALUE, 0), new Team("C", 9, 8), new Team("D", 9, 1)));
        faultyTest.sort(faulty);
        System.out.println("Faulty comparator demonstration (do not use): " + faultyTest);
        System.out.println("Bisons ranks above Panthers alphabetically because points and goal difference tie.");
    }
}
