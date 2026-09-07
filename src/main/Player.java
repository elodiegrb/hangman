package main;

public class Player {

    private String name;
    private int score;
    private int totalGuesses;
    private int totalCorrectGuesses;
    private double accuracy;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.totalGuesses = 0;
        this.totalCorrectGuesses = 0;
        this.accuracy = 0.0;
    }

    //for loading in player details from file
    public Player(String name, int score, int totalGuesses, int totalCorrectGuesses) {
        this.name = name;
        this.score = score;
        this.totalGuesses = totalGuesses;
        this.totalCorrectGuesses = totalCorrectGuesses;
        this.accuracy = 0.0;
        updateAccuracy();
    }

    public void incrementScore() {
        score++;
    }

    public void incrementTotalGuesses() {
        totalGuesses++;
    }

    public void incrementTotalCorrectGuesses() {
        totalCorrectGuesses++;
    }

    public void updateAccuracy() {
        if (totalGuesses == 0) {
            accuracy = 0.0;
        } else {
            accuracy = (double) totalCorrectGuesses / totalGuesses;
            accuracy = Math.round(accuracy * 100.0);
        }
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public int getTotalGuesses() {
        return totalGuesses;
    }

    public int getTotalCorrectGuesses() {
        return totalCorrectGuesses;
    }

    public double getAccuracy() {
        return accuracy;
    }
}