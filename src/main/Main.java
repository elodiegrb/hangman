import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main() {

        printTitle();
        menu();
    }

    public static void printTitle() {
        System.out.println("""
                  ____         ____      _         _      __   __U _____ u   ____          _   _      _      _   _     ____    __  __      _      _   _    \s
                  |___"\\      U|  _"\\ u  |"|    U  /"\\  u  \\ \\ / /\\| ___"|/U |  _"\\ u      |'| |'| U  /"\\  u | \\ |"| U /"___|uU|' \\/ '|uU  /"\\  u | \\ |"|   \s
                  U __) |     \\| |_) |/U | | u   \\/ _ \\/    \\ V /  |  _|"   \\| |_) |/     /| |_| |\\ \\/ _ \\/ <|  \\| |>\\| |  _ /\\| |\\/| |/ \\/ _ \\/ <|  \\| |>  \s
                  \\/ __/ \\     |  __/   \\| |/__  / ___ \\   U_|"|_u | |___    |  _ <       U|  _  |u / ___ \\ U| |\\  |u | |_| |  | |  | |  / ___ \\ U| |\\  |u  \s
                  |_____|u     |_|       |_____|/_/   \\_\\    |_|   |_____|   |_| \\_\\       |_| |_| /_/   \\_\\ |_| \\_|   \\____|  |_|  |_| /_/   \\_\\ |_| \\_|   \s
                  <<  //       ||>>_     //  \\\\  \\\\    >>.-,//|(_  <<   >>   //   \\\\_      //   \\\\  \\\\    >> ||   \\\\,-._)(|_  <<,-,,-.   \\\\    >> ||   \\\\,-.\s
                 (__)(__)     (__)__)   (_")("_)(__)  (__)\\_) (__)(__) (__) (__)  (__)    (_") ("_)(__)  (__)(_")  (_/(__)__)  (./  \\.) (__)  (__)(_")  (_/ \s
                """);
    }

    public static void menu() {

        while (true) {
            File file = new File("saves.txt");
            System.out.println("Start new game [s], continue saved game [c] or quit [q] ?");
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            s = s.trim();

            if (s.equalsIgnoreCase("s")) {
                if (file.exists()) {
                    System.out.println("Starting a new game will overwrite your previous saved game. Do you want to continue? [y/n]");
                    s = scanner.nextLine().trim().toLowerCase();
                    if (s.equalsIgnoreCase("y")) {
                        signUp();
                        break;
                    }
                } else {
                    signUp();
                    break;
                }
                } else if (s.equalsIgnoreCase("c")) {
                if (!file.exists()) {
                    System.out.println("No saved game found.");
                } else {
                    Game game = new Game();
                    game.loadGame();
                    break;
                }
            } else if (s.equalsIgnoreCase("q")) {
                quitGame();
            }
        }
    }

    public static void signUp() {
        Player player1 = getName(1);
        Player player2 = getName(2);
        Game game = new Game(player1, player2);
        game.setupGame();
    }

    public static Player getName(int playerNo) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Enter name of player " + playerNo);
            String name = scanner.nextLine();
            name = name.trim();
            boolean valid = validatePlayerName(name);
            while (!valid) {
                System.out.println("Entered name not valid. Name must only contain letters. Please try again:");
                name = scanner.nextLine();
                valid = validatePlayerName(name);
            }
            Player player = new Player(name);
            return player;
        }
    }

    public static boolean validatePlayerName(String name) {

        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(name);
        boolean matchFound = matcher.find();
        if (matchFound) {
            return true;
        }
        return false;
    }

    public static void quitGame() {
        System.exit(0);
    }

}