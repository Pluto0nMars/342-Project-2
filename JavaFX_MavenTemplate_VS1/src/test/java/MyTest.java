import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;



class MyTest {
    private KenoLogic logic;


    @BeforeEach
    public void setup() {
        logic = new KenoLogic();
    }

    @Test
    public void testCalculatePrize() {
        assertEquals(100000, logic.calculatePrize(10, 10));
        assertEquals(450, logic.calculatePrize(10, 8));
        assertEquals(0, logic.calculatePrize(10, 0));
        assertEquals(150, logic.calculatePrize(4, 4));
        assertEquals(10, logic.calculatePrize(2, 2));
        assertEquals(2, logic.calculatePrize(1, 1));
    }

    @Test
    public void testAddToTopGames() {
        logic.addToTopGames(100);
        logic.addToTopGames(50);
        logic.addToTopGames(200);
        List<Integer> top = logic.getTopGames();
        assertEquals(3, top.size());
        assertEquals(200, top.get(0));
        assertEquals(100, top.get(1));
        assertEquals(50, top.get(2));
    }
    @Test
    public void testCalculatePrize_AllSpots() {
        // Spot 1
        assertEquals(2, logic.calculatePrize(1, 1));
        assertEquals(0, logic.calculatePrize(1, 0));

        assertEquals(10, logic.calculatePrize(2, 2));
        assertEquals(1, logic.calculatePrize(2, 1));
        assertEquals(0, logic.calculatePrize(2, 0));

        assertEquals(150, logic.calculatePrize(4, 4));
        assertEquals(5, logic.calculatePrize(4, 3));
        assertEquals(1, logic.calculatePrize(4, 2));
        assertEquals(0, logic.calculatePrize(4, 1));
        assertEquals(0, logic.calculatePrize(4, 0));

        assertEquals(100000, logic.calculatePrize(10, 10));
        assertEquals(4250, logic.calculatePrize(10, 9));
        assertEquals(450, logic.calculatePrize(10, 8));
        assertEquals(40, logic.calculatePrize(10, 7));
        assertEquals(15, logic.calculatePrize(10, 6));
        assertEquals(8, logic.calculatePrize(10, 5));
        assertEquals(6, logic.calculatePrize(10, 4));
        assertEquals(4, logic.calculatePrize(10, 3));
        assertEquals(2, logic.calculatePrize(10, 2));
        assertEquals(0, logic.calculatePrize(10, 1));
        assertEquals(0, logic.calculatePrize(10, 0));
    }

    @Test
    public void testAddTopGames(){
        logic.addToTopGames(100);
        logic.addToTopGames(50);
        logic.addToTopGames(1000);


        List<Integer> top = logic.getTopGames();
        assertEquals(1000, top.get(0));
        assertEquals(50, top.get(2));
        assertEquals(100, top.get(1));
    }

    @Test
    public void testCalculatePrize_InvalidMatches() {
        assertEquals(0, logic.calculatePrize(1, 2));
        assertEquals(0, logic.calculatePrize(2, 3));
        assertEquals(0, logic.calculatePrize(4, 5));
        assertEquals(0, logic.calculatePrize(10, 11));
    }

    @Test
    public void testCalculatePrize_NegativeValues() {
        assertEquals(0, logic.calculatePrize(1, -1));
        assertEquals(0, logic.calculatePrize(2, -3));
    }

    @Test
    public void testAddToTopGames_EmptyStart() {
        assertTrue(logic.getTopGames().isEmpty());
    }

    @Test
    public void testAddToTopGames_SameAsMaxSize() {
        for (int i = 1; i <= 11; i++) {
            logic.addToTopGames(i);
        }
        List<Integer> top = logic.getTopGames();
        assertEquals(11, top.size());
    }

    @Test
    public void testCalculatePrize_Spot2Boundary() {
        assertEquals(10, logic.calculatePrize(2, 2));
        assertEquals(1, logic.calculatePrize(2, 1));
        assertEquals(0, logic.calculatePrize(2, 0));
    }

    @Test
    public void testCalculatePrize_Spot10Boundary() {
        assertEquals(100000, logic.calculatePrize(10, 10));
        assertEquals(4250, logic.calculatePrize(10, 9));
        assertEquals(450, logic.calculatePrize(10, 8));
        assertEquals(0, logic.calculatePrize(10, 1));
        assertEquals(0, logic.calculatePrize(10, 0));
    }
    @Test
    public void testCalculatePrize_Spot1Boundary() {

        assertEquals(2, logic.calculatePrize(1, 1));
    }

    @Test
    public void testCalculatePrize_Spot4Boundary() {
        assertEquals(150, logic.calculatePrize(4, 4));
        assertEquals(5, logic.calculatePrize(4, 3));
        assertEquals(1, logic.calculatePrize(4, 2));
    }


    @Test
    public void testAddToTopGames_MaxLimit() {
        // Add 15 values (more than 11)
        for (int i = 1; i <= 15; i++) {
            logic.addToTopGames(i * 10);
        }

        List<Integer> top = logic.getTopGames();
        assertEquals(11, top.size());
        assertEquals(150, top.get(0));
        assertEquals(50, top.get(top.size() - 1));
    }

    @Test
    public void testAddToTopGames_Duplicates() {
        logic.addToTopGames(100);
        logic.addToTopGames(100);
        logic.addToTopGames(200);
        logic.addToTopGames(50);

        List<Integer> top = logic.getTopGames();
        assertEquals(List.of(200, 100, 100, 50), top);
    }

    @Test
    public void testCalculatePrize_InvalidSpots() {
        assertEquals(0, logic.calculatePrize(0, 1));
        assertEquals(0, logic.calculatePrize(-5, 3));
        assertEquals(0, logic.calculatePrize(3, 2));
        assertEquals(0, logic.calculatePrize(8, 4));
    }

    @Test
    public void testCalculatePrize_PreferenceOverMap() {
        assertEquals(0, logic.calculatePrize(4, 1));
    }

    @Test
    public void testTopGames_OrderStability() {
        logic.addToTopGames(300);
        logic.addToTopGames(100);
        logic.addToTopGames(200);
        logic.addToTopGames(250);

        List<Integer> top = logic.getTopGames();
        assertEquals(List.of(300, 250, 200, 100), top);
    }

    @Test
    public void testAddToTopGames_LimitsTo11() {
        for (int i = 1; i <= 20; i++) {
            logic.addToTopGames(i);
        }
        List<Integer> top = logic.getTopGames();
        assertEquals(11, top.size());
        assertEquals(20, top.get(0));
        assertEquals(10, top.get(10));
    }

    @Test
    public void testAddToTopGames_DuplicatesAgain() {
        logic.addToTopGames(100);
        logic.addToTopGames(100);
        logic.addToTopGames(50);
        List<Integer> top = logic.getTopGames();
        assertEquals(3, top.size());
        assertEquals(100, top.get(0));
        assertEquals(100, top.get(1));
    }

    @Test
    public void testCalculatePrize_MatchesGreaterThanSpots() {
        assertEquals(0, logic.calculatePrize(2, 3));
        assertEquals(0, logic.calculatePrize(1, 5));
    }

    @Test
    public void testCalculatePrize_SpotsZero() {
        assertEquals(0, logic.calculatePrize(0, 0));
        assertEquals(0, logic.calculatePrize(0, 5));
    }

    @Test
    public void testCalculatePrize_MatchesZeroForNon10Spots() {
        assertEquals(0, logic.calculatePrize(4, 0));
        assertEquals(0, logic.calculatePrize(2, 0));
        assertEquals(0, logic.calculatePrize(1, 0));
    }

    @Test
    public void testAddToTopGames_DescendingInput() {
        logic.addToTopGames(300);
        logic.addToTopGames(200);
        logic.addToTopGames(100);
        List<Integer> top = logic.getTopGames();
        assertEquals(List.of(300, 200, 100), top);
    }

    @Test
    public void testAddToTopGames_SmallValues() {
        logic.addToTopGames(0);
        logic.addToTopGames(-10);
        logic.addToTopGames(5);
        List<Integer> top = logic.getTopGames();
        assertEquals(3, top.size());
        assertEquals(5, top.get(0));
        assertEquals(0, top.get(1));
        assertEquals(-10, top.get(2));
    }

    @Test
    public void testCalculatePrize_UnlistedSpotValue() {
        // Spots not 1, 2, 4, or 10 should return 0 always
        assertEquals(0, logic.calculatePrize(3, 2));
        assertEquals(0, logic.calculatePrize(6, 3));
    }


}
