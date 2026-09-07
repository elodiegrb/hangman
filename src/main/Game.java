import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Game {

    private Player player1;
    private Player player2;
    private Scanner input;
    private List<Character> correctGuesses = new ArrayList<>();
    private List<Character> mistakes = new ArrayList<>();
    private int mistakesCounter = 0;

    // constructor for standard use
    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.input = new Scanner(System.in);
    }

    // constructor for loading game
    public Game() {
        this.input = new Scanner(System.in);
    }

    public void setupGame() {
        Player guessingPlayer = player1;
        Player otherPlayer = player2;
        Phrase phrase = getPhrase(guessingPlayer, otherPlayer);
        playGame(guessingPlayer, otherPlayer, phrase);
    }

    public void setupGameFromLoad(Player guessingPlayer, Player otherPlayer) {
        Phrase newPhrase = getPhrase(guessingPlayer, otherPlayer);
        playGame(guessingPlayer, otherPlayer, newPhrase);
    }

    public void playGame(Player guessingPlayer, Player otherPlayer, Phrase phrase) {
        if (phrase != null) {
            System.out.print(guessingPlayer.getName() + "! It's time to guess...");
            displayGame(phrase);

            while (true) {
                System.out.println("Category: " + phrase.getHint());
                System.out.println("List of available commands: save and continue [save], save and quit [quit]");
                System.out.print("Incorrect guesses (" + mistakesCounter + "/6):");
                for (char m : mistakes) {
                    System.out.print(" " + m + " ");
                }
                if (!enterLetter(phrase, true, guessingPlayer, otherPlayer)) {
                    break;
                }

                if (isSolved(phrase)) {
                    guessingPlayer.incrementScore();
                    displayGame(phrase);
                    System.out.println("Solved! " + guessingPlayer.getName() + " wins this round and gains one point!");
                } else if (mistakesCounter > 5) {
                    otherPlayer.incrementScore();
                    displayGame(phrase);
                    System.out.println(guessingPlayer.getName() + " has ran out of guesses! " + otherPlayer.getName() + " wins this round and gains one point!");
                    System.out.println("The answer was: " + phrase.getPhrase());
                }

                if (isSolved(phrase) || mistakesCounter > 5) {
                    System.out.println(" -- Points -- ");
                    System.out.println(player1.getName() + ": " + player1.getScore());
                    System.out.println(player2.getName() + ": " + player2.getScore());
                    Player tempPlayer = guessingPlayer;
                    guessingPlayer = otherPlayer;
                    otherPlayer = tempPlayer;
                    correctGuesses.clear();
                    mistakes.clear();
                    mistakesCounter = 0;
                    Phrase newPhrase = new Phrase(" ", " ");
                    saveGame(newPhrase, guessingPlayer, otherPlayer);
                    Main.menu();
                    break;
                }

                displayGame(phrase);
            }
        }
    }

    public Phrase getPhrase(Player guessingPlayer, Player otherPlayer) {
        System.out.print(otherPlayer.getName() + "! Enter a word or phrase for " + guessingPlayer.getName() + " to guess: ");
        String p = validatePhrase();
        String h = getHint();

        return new Phrase(p, h);
    }

    public String validatePhrase() {
        String p = input.nextLine().trim();

        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

        Matcher matcher = pattern.matcher(p);
        boolean matchFound = matcher.find();

        while (!matchFound || p.length() > 200) {
            System.out.print("Your word or phrase can only contain letters and spaces and must be 200 characters or less. Try again: ");
            p = input.nextLine().trim();
            matcher = pattern.matcher(p);
            matchFound = matcher.find();
        }
        p = p.toLowerCase().replaceAll(" +", " ");
        return p;
    }

    public String getHint() {
        System.out.println("What category is your word or phrase?");
        System.out.println("Suggestions: film, tv, music, book, sport, animal, person, brand, none");
        System.out.println("Enter a category:");
        String h = input.nextLine().trim();

        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");

        Matcher matcher = pattern.matcher(h);
        boolean matchFound = matcher.find();

        while (!matchFound || h.length() > 20) {
            System.out.print("Your category can only contain letters and spaces and must be 20 characters or less. Try again: ");
            h = input.nextLine().trim();
            matcher = pattern.matcher(h);
            matchFound = matcher.find();
        }

        h = h.toLowerCase().replaceAll(" +", " ");
        return h;
    }

    private void displayGame(Phrase phrase) {
        List<Character> characters = phrase.getCharacters();
        System.out.println();
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);
            if (Objects.equals(character, ' ')) {
                System.out.print("    ");
            } else if (correctGuesses.contains(character)){
                System.out.print(character + " ");
            } else {
                System.out.print("_ ");
            }
        }
        System.out.println();
    }

    private boolean enterLetter(Phrase phrase, boolean quitReturnsToMainMenu, Player guessingPlayer, Player otherPlayer) {
        List<Character> characters = phrase.getCharacters();
        System.out.println();
        System.out.println("Enter letter or command: ");
        String s = input.nextLine().trim().toLowerCase();
        if (s.length() != 1) {
            if (s.equals("quit")) {
                saveGame(phrase, guessingPlayer, otherPlayer);
                Main.menu();
                return false;
            } else if (s.equals("save")){
                saveGame(phrase, guessingPlayer, otherPlayer);
                return true;
            } else {
                System.out.println("That's not a valid letter or command!");
                return true;
            }
        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z]$");
            Matcher matcher = pattern.matcher(s);
            boolean matchFound = matcher.find();
            if (!matchFound) {
                System.out.println("That's not a valid letter or command!");
            } else {
                char l = s.charAt(0);
                if (characters.contains(l) && !correctGuesses.contains(l)) { //correct guess
                    correctGuesses.add(l);
                    //update stats
                    return true;
                } else if (characters.contains(l) && correctGuesses.contains(l)) {
                    System.out.println("You've already guessed this letter!");
                    return true; // if entered info is the same as the game correctGuesses do nothing
                } else if (!characters.contains(l) && !mistakes.contains(l)) { // incorrect guess
                    System.out.println("Wrong letter!");
                    mistakes.add(l);
                    mistakesCounter++;
                    //update stats
                    return true;
                } else { // already guessed incorrect letter
                    System.out.println("You've already guessed this letter!");
                    return true;
                }

            }
        }
        return true;
    }

    public boolean isSolved(Phrase phrase) {
        for (char c : phrase.getCharacters()) {
            if (!Objects.equals(c, ' ')) {
                if (!correctGuesses.contains(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void saveGame(Phrase phrase, Player guessingPlayer, Player otherPlayer) {

        File file = new File("saves.txt");

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write("correct guesses\n");
            for (Character character : correctGuesses) {
                bw.write(character);
            }

            bw.write("\nincorrect guesses\n");
            for (Character character : mistakes) {
                bw.write(character);
            }

            bw.write("\nmistakes count\n");
            bw.write(mistakesCounter + "\n");

            bw.write("phrase\n");
            String word = phrase.getPhrase();
            bw.write(word + "\n");

            bw.write("category\n");
            String hint = phrase.getHint();
            bw.write(hint + "\n");

            bw.write("guessing player\n");
            bw.write(guessingPlayer.getName() + "\n" + guessingPlayer.getScore() + "\n" +guessingPlayer.getTotalGuesses() + "\n" + guessingPlayer.getTotalCorrectGuesses() +"\n");

            bw.write("other player\n");
            bw.write(otherPlayer.getName() + "\n" + otherPlayer.getScore() + "\n" + otherPlayer.getTotalGuesses() + "\n" + otherPlayer.getTotalCorrectGuesses() +"\n");

            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Game saved successfully!");
    }

    public void loadGame() {
        File file = new File("saves.txt");

        if (!file.exists()) {
            System.out.println("No saved game found.");
            return;
        }

        try (Scanner fileReader = new Scanner(file)) {

            fileReader.nextLine();
            String correctGuessesLine = fileReader.nextLine();
            for (char c : correctGuessesLine.toCharArray()) {
                correctGuesses.add(c);
            }

            fileReader.nextLine();
            String mistakesLine = fileReader.nextLine();
            for (char c : mistakesLine.toCharArray()) {
                mistakes.add(c);
            }

            fileReader.nextLine();
            mistakesCounter = Integer.parseInt(fileReader.nextLine());

            fileReader.nextLine();
            String loadedPhrase = fileReader.nextLine();

            fileReader.nextLine();
            String loadedHint = fileReader.nextLine();

            fileReader.nextLine();
            String guessingName = fileReader.nextLine();
            int guessingScore = Integer.parseInt(fileReader.nextLine());
            int guessingGuesses = Integer.parseInt(fileReader.nextLine());
            int guessingCorrectGuesses = Integer.parseInt(fileReader.nextLine());
            player1 = new Player(guessingName, guessingScore, guessingGuesses, guessingCorrectGuesses );

            fileReader.nextLine();
            String otherName = fileReader.nextLine();
            int otherScore = Integer.parseInt(fileReader.nextLine());
            int otherGuesses = Integer.parseInt(fileReader.nextLine());
            int otherCorrectGuesses = Integer.parseInt(fileReader.nextLine());
            player2 = new Player(otherName, otherScore, otherGuesses, otherCorrectGuesses );

            Player guessingPlayer = player1;
            Player otherPlayer = player2;

            Phrase phrase = new Phrase(loadedPhrase, loadedHint);

            System.out.println("Saved game loaded successfully.");
            System.out.println();

            if (loadedPhrase.equals(" ")) {
                setupGameFromLoad(guessingPlayer, otherPlayer);
            } else {
                playGame(guessingPlayer, otherPlayer, phrase);
            }

        } catch (Exception e) {
            System.out.println("Could not load saved game. The file may be missing or corrupted.");
        }
    }

}
