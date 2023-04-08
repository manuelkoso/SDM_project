import it.units.sdm.project.core.board.Stone;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class StoneTests {
    Stone blackStone = new Stone(Stone.Color.BLACK);

    @Test
    void testColorGetter() {
        assertEquals(blackStone.getColor(), Stone.Color.BLACK);
    }

    @Test
    void testEquals() {
        Stone newWhiteStone = new Stone(Stone.Color.WHITE);
        Stone newBlackStone = new Stone(Stone.Color.BLACK);
        assertEquals(blackStone, newBlackStone);
        assertNotEquals(blackStone, newWhiteStone);
    }

}
