package application.Model;


public class Sentence{
    int paragraphNo;
    int number;
    double score;
    int noOfWords;
    public String value;

    Sentence(int number, String value, int paragraphNumber){
        this.number = number;
        this.value = new String(value);
        noOfWords = value.split("\\s+").length;
        score = 0.0;
        this.paragraphNo =paragraphNumber;
    }
}