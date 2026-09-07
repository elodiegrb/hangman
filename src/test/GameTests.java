import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class GameTests {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    public void restoreSystemOut() {
        System.setOut(originalOut);
    }

    @Test
    void testValidatePhrase() {
        String input = "Hello World\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();
        assertEquals("hello world", game.validatePhrase());
    }

    @Test
    void testRemovingExtraSpaces() {
        String input = "hello    world\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();
        assertEquals("hello world", game.validatePhrase());
    }

    @Test
    void testRejectingInvalidCharacters() {
        String input = "hello123\nhello\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();
        game.validatePhrase();
        String expectedOutput = "Your word or phrase can only contain letters and spaces and must be 200 characters or less. Try again: ";
        assertEquals(expectedOutput, outputStream.toString());
        String result = game.validatePhrase();
        assertEquals("hello", result);
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    void testGetHint() {
        String input = "Film\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();
        assertEquals("film", game.getHint());
    }

    @Test
    void getHintRemovesExtraSpaces() {
        String input = "hello    world\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        Game game = new Game();
        assertEquals("hello world", game.getHint());
    }

    @Test
    void isSolvedReturnsTrue() throws Exception {
        Player player1 = new Player("playerone");
        Player player2 = new Player("playertwo");

        Game game = new Game(player1, player2);
        Phrase phrase = new Phrase("hello", "world");

        List<Character> correctGuesses = getCorrectGuesses(game);

        correctGuesses.add('h');
        correctGuesses.add('e');
        correctGuesses.add('l');
        correctGuesses.add('o');

        assertTrue(game.isSolved(phrase));
    }

    @Test
    void isSolvedReturnsFalse() throws Exception {
        Player player1 = new Player("playerone");
        Player player2 = new Player("playertwo");

        Game game = new Game(player1, player2);
        Phrase phrase = new Phrase("hello", "none");

        List<Character> correctGuesses = getCorrectGuesses(game);

        correctGuesses.add('h');
        correctGuesses.add('e');
        correctGuesses.add('l');

        assertFalse(game.isSolved(phrase));
    }

    @SuppressWarnings("unchecked")
    private List<Character> getCorrectGuesses(Game game) throws Exception {
        Field field = Game.class.getDeclaredField("correctGuesses");
        field.setAccessible(true);

        return (List<Character>) field.get(game);
    }
}
