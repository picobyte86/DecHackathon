package application.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Iterator;
import java.util.Collections;


class SumModel {
    File file;
    FileInputStream in;
    FileOutputStream out;
    ArrayList<Sentence> sentences, contentSummary;
    ArrayList<Paragraph> paragraphs;
    int sentNo, paraNo;

    double[][] intersectionMatrix;
    LinkedHashMap<Sentence, Double> dictionary;


    SumModel(File file) {
        this.file = file;
        in = null;
        out = null;
        sentNo = 0;
        paraNo = 0;
    }

    void init() {
        sentences = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();
        dictionary = new LinkedHashMap<Sentence, Double>();
        sentNo = 0;
        paraNo = 0;
        try {
            in = new FileInputStream(file);
            out = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void extractSentenceFromContext() {
        int nextChar, j = 0;
        int prevChar = -1;
        try {
            while ((nextChar = in.read()) != -1) {
                j = 0;
                char[] temp = new char[100000];
                while ((char) nextChar != '.') {
                    temp[j] = (char) nextChar;
                    if ((nextChar = in.read()) == -1) {
                        break;
                    }
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') {
                        paraNo++;
                    }
                    j++;
                    prevChar = nextChar;
                }

                sentences.add(new Sentence(sentNo, (new String(temp)).trim(), paraNo));
                sentNo++;
                prevChar = nextChar;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void groupSentencesIntoParagraphs() {
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        for (int i = 0; i < sentNo; i++) {
            if (sentences.get(i).paragraphNo == paraNum) {

            } else {
                paragraphs.add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }
            paragraph.sentences.add(sentences.get(i));
        }

        paragraphs.add(paragraph);
    }

    double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.value.split("\\s+")) {
            for (String str2Word : str2.value.split("\\s+")) {
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {
                    commonCount++;
                }
            }
        }

        return commonCount;
    }

    void createIntersectionMatrix() {
        intersectionMatrix = new double[sentNo][sentNo];
        for (int i = 0; i < sentNo; i++) {
            for (int j = 0; j < sentNo; j++) {

                if (i <= j) {
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.noOfWords + str2.noOfWords) / 2);
                } else {
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                }

            }
        }
    }

    void createDictionary() {
        for (int i = 0; i < sentNo; i++) {
            double score = 0;
            for (int j = 0; j < sentNo; j++) {
                score += intersectionMatrix[i][j];
            }
            dictionary.put(sentences.get(i), score);
            ((Sentence) sentences.get(i)).score = score;
        }
    }

    void createSummary() {

        for (int j = 0; j <= paraNo; j++) {
            int primary_set = paragraphs.get(j).sentences.size() / 5;

            Collections.sort(paragraphs.get(j).sentences, new SenComp());
            for (int i = 0; i <= primary_set; i++) {
                contentSummary.add(paragraphs.get(j).sentences.get(i));
            }
        }

        Collections.sort(contentSummary, new SumComp());

    }


    void printSentences() {
        for (Sentence sentence : sentences) {
            System.out.println(sentence.number + " => " + sentence.value + " => " + sentence.noOfWords + " => " + sentence.paragraphNo);
        }
    }

    void printIntersectionMatrix() {
        for (int i = 0; i < sentNo; i++) {
            for (int j = 0; j < sentNo; j++) {
                System.out.print(intersectionMatrix[i][j] + "    ");
            }
            System.out.print("\n");
        }
    }

    void printDicationary() {
        Set set = dictionary.entrySet();
        Iterator i = set.iterator();
        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            System.out.print(((Sentence) me.getKey()).value + ": ");
            System.out.println(me.getValue());
        }
    }

    void printSummary() {
        System.out.println("no of paragraphs = " + paraNo);
        for (Sentence sentence : contentSummary) {
            System.out.println(sentence.value);
        }
    }

    double getWordCount(ArrayList<Sentence> sentenceList) {
        double wordCount = 0.0;
        for (Sentence sentence : sentenceList) {
            wordCount += (sentence.value.split(" ")).length;
        }
        return wordCount;
    }

    void printStats() {
        System.out.println("number of words in Context : " + getWordCount(sentences));
        System.out.println("number of words in Summary : " + getWordCount(contentSummary));
        System.out.println("Commpression : " + getWordCount(contentSummary) / getWordCount(sentences));
    }
}