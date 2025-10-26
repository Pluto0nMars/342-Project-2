import java.util.*;

public class KenoLogic {
    public static final TreeMap<Integer, Integer> prizesMap = new TreeMap<>();
    static {
        prizesMap.put(10, 10000);
        prizesMap.put(9, 4250);
        prizesMap.put(8, 450);
        prizesMap.put(7, 40);
        prizesMap.put(6, 15);
        prizesMap.put(5, 8);
        prizesMap.put(4, 6);
        prizesMap.put(3, 4);
        prizesMap.put(2, 2);
        prizesMap.put(1, 0);
        prizesMap.put(0, 0);
    }
    private List<Integer> topGames = new ArrayList<>();
    public int calculatePrize(int spots, int matches) {
        if (spots == 10) {
            if (matches == 10) return 100000;
            if (matches == 9) return 4250;
            if (matches == 8) return 450;
            if (matches == 7) return 40;
            if (matches == 6) return 15;
            if (matches == 5) return 8;
            if (matches == 4) return 6;
            if (matches == 3) return 4;
            if (matches == 2) return 2;
            if (matches == 0) return 0;
        } else if (spots == 4) {
            if (matches == 4) return 150;
            if (matches == 3) return 5;
            if (matches == 2) return 1;
        } else if (spots == 2) {
            if (matches == 2) return 10;
            if (matches == 1) return 1;
        } else if (spots == 1) {
            if (matches == 1) return 2;
        }
        return 0;
    }

    public void addToTopGames(int game) {
        topGames.add(game);
        topGames.sort((a, b) -> Integer.compare(b, a));
        if (topGames.size() > 11) topGames.remove(topGames.size() - 1);
    }

    public List<Integer> getTopGames() {
        return topGames;
    }

}
