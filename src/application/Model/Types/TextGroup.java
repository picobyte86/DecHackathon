package application.Model.Types;

import java.util.ArrayList;

public class TextGroup {
    private ArrayList<String> words;
    private double weight;
    public TextGroup(ArrayList<String> words, double weight) {
        this.weight = weight;
        this.words = words;
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "{" + words + "," + weight + "}";
    }
}
