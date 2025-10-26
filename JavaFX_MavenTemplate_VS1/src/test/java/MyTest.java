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



    //void test
}
