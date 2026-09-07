import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTests {

    @Test
    void testValidatePlayerName() {
        String name1 = "Player";
        String name2 = " ";
        String name3 = "@";
        String name4 = "Hello123";

        assertTrue(Main.validatePlayerName(name1), name1);
        assertFalse(Main.validatePlayerName(name2), name2);
        assertFalse(Main.validatePlayerName(name3), name3);
        assertFalse(Main.validatePlayerName(name4), name4);
    }
}
