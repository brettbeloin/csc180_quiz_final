package com.csc180.brettbeloin.models;

public class Game {
    private int correct_guesses;
    private int wrong_guesses;
    private double score;

    public Game(int correct_guesses, int wrong_guesses, double score) {
        setCorrect_guesses(correct_guesses);
        setWrong_guesses(wrong_guesses);
        setScore(score);
    }

    public int getCorrect_guesses() {
        return correct_guesses;
    }

    public int getWrong_guesses() {
        return wrong_guesses;
    }

    public double getScore() {
        return score;
    }

    public void setCorrect_guesses(int correct_guesses) {
        this.correct_guesses = correct_guesses;
    }

    public void setWrong_guesses(int wrong_guesses) {
        this.wrong_guesses = wrong_guesses;
    }

    public void setScore(double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("You got: \n%d correct \n%d wrong \n\nyour score is: %.02f", getCorrect_guesses(),
                getWrong_guesses(), getScore());
    }

}
