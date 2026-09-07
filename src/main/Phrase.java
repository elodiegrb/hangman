import java.util.ArrayList;
import java.util.List;

public class Phrase {

    private String phrase;
    private List<Character> characters;
    private String hint;

    public Phrase(String phrase, String hint) {
        this.phrase = phrase;
        this.characters = splitPhrase(phrase);
        this.hint = hint;
    }

    public List<Character> splitPhrase(String phrase) {
        List<Character> phraseCharacters = new ArrayList<>();
        for (char c : phrase.toCharArray()) {
            phraseCharacters.add(c);
        }
        return phraseCharacters;
    }

    public String getPhrase() {
        return phrase;
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public String getHint() {
        return hint;
    }

}
