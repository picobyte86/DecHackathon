package application.Model;

import java.util.ArrayList;

public class Paragraph {
    int number;
    ArrayList<Sentence> sentences;

    Paragraph(int number){
        this.number = number;
        sentences = new ArrayList<Sentence>();
    }
}

class Sentence{
    int paragraphNo;
    int number;
    double score;
    int noOfWords;
    String value;

    Sentence(int number, String value, int paragraphNumber){
        this.number = number;
        this.value = new String(value);
        noOfWords = value.split("\\s+").length;
        score = 0.0;
        this.paragraphNo =paragraphNumber;
    }
}
