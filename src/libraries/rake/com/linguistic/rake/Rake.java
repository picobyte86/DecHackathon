package libraries.rake.com.linguistic.rake;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class Rake {
    String language;
    String stopWordsPattern;

    public Rake(String language) throws FileNotFoundException {
        this.language = language;
        File file = new File("src/libraries/rake/data/stopwords/languages/" + language + ".txt");
        InputStream stream = new FileInputStream(file);
        if (stream != null) {
            try {
                ArrayList<String> stopWords = new ArrayList();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));

                String line;
                while((line = bufferedReader.readLine()) != null) {
                    stopWords.add(line.trim());
                }

                ArrayList<String> regexList = new ArrayList();
                Iterator var7 = stopWords.iterator();

                while(var7.hasNext()) {
                    String word = (String)var7.next();
                    String regex = "\\b" + word + "(?![\\w-])";
                    regexList.add(regex);
                }

                this.stopWordsPattern = String.join("|", regexList);
            } catch (Exception var10) {
                throw new Error("An error occurred reading stop words for language " + language);
            }
        } else {
            throw new Error("Could not find stop words required for language " + language);
        }
    }

    private String[] getSentences(String text) {
        return text.split("[.!?,;:\\t\\\\\\\\\"\\\\(\\\\)\\\\'\\u2019\\u2013]|\\\\s\\\\-\\\\s");
    }

    private String[] separateWords(String text, int size) {
        String[] split = text.split("[^a-zA-Z0-9_\\\\+/-\\\\]");
        ArrayList<String> words = new ArrayList();
        String[] var5 = split;
        int var6 = split.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String word = var5[var7];
            String current = word.trim().toLowerCase();
            int len = current.length();

            if (len > size && len > 0 && !isNumeric(current)) {
                words.add(current);
            }
        }

        return (String[])words.toArray(new String[words.size()]);
    }
    private boolean isNumeric(CharSequence sequence) {
        for (int i=0;i<sequence.length();i++) {
            if (!Character.isDigit(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
    private String[] getKeywords(String[] sentences) {
        ArrayList<String> phraseList = new ArrayList();
        String[] var3 = sentences;
        int var4 = sentences.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String sentence = var3[var5];
            String temp = sentence.trim().replaceAll(this.stopWordsPattern, "|");
            String[] phrases = temp.split("\\|");
            String[] var9 = phrases;
            int var10 = phrases.length;

            for(int var11 = 0; var11 < var10; ++var11) {
                String phrase = var9[var11];
                phrase = phrase.trim().toLowerCase();
                if (phrase.length() > 0) {
                    phraseList.add(phrase);
                }
            }
        }

        return (String[])phraseList.toArray(new String[phraseList.size()]);
    }

    private LinkedHashMap<String, Double> calculateWordScores(String[] phrases) {
        LinkedHashMap<String, Integer> wordFrequencies = new LinkedHashMap();
        LinkedHashMap<String, Integer> wordDegrees = new LinkedHashMap();
        LinkedHashMap<String, Double> wordScores = new LinkedHashMap();
        String[] var5 = phrases;
        int var6 = phrases.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            String phrase = var5[var7];
            String[] words = this.separateWords(phrase, 0);
            int length = words.length;
            int degree = length - 1;
            String[] var12 = words;
            int var13 = words.length;

            for(int var14 = 0; var14 < var13; ++var14) {
                String word = var12[var14];
                wordFrequencies.put(word, (Integer)wordDegrees.getOrDefault(word, 0) + 1);
                wordDegrees.put(word, (Integer)wordFrequencies.getOrDefault(word, 0) + degree);
            }
        }

        Iterator var16 = wordFrequencies.keySet().iterator();

        while(var16.hasNext()) {
            String item = (String)var16.next();
            wordDegrees.put(item, (Integer)wordDegrees.get(item) + (Integer)wordFrequencies.get(item));
            wordScores.put(item, (double)(Integer)wordDegrees.get(item) / ((double)(Integer)wordFrequencies.get(item) * 1.0D));
        }

        return wordScores;
    }

    private LinkedHashMap<String, Double> getCandidateKeywordScores(String[] phrases, LinkedHashMap<String, Double> wordScores) {
        LinkedHashMap<String, Double> keywordCandidates = new LinkedHashMap();
        String[] var4 = phrases;
        int var5 = phrases.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String phrase = var4[var6];
            double score = 0.0D;
            String[] words = this.separateWords(phrase, 0);
            String[] var11 = words;
            int var12 = words.length;

            for(int var13 = 0; var13 < var12; ++var13) {
                String word = var11[var13];
                score += (Double)wordScores.get(word);
            }

            keywordCandidates.put(phrase, score);
        }

        return keywordCandidates;
    }

    private LinkedHashMap<String, Double> sortHashMap(LinkedHashMap<String, Double> map) {
        LinkedHashMap<String, Double> result = new LinkedHashMap();
        List<Entry<String, Double>> list = new LinkedList(map.entrySet());
        Collections.sort(list, Comparator.comparing(Entry::getValue));
        Collections.reverse(list);
        Iterator it = list.iterator();

        while(it.hasNext()) {
            Entry<String, Double> entry = (Entry)it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public LinkedHashMap<String, Double> getKeywordsFromText(String text) {
        String[] sentences = this.getSentences(text);
        String[] keywords = this.getKeywords(sentences);
        LinkedHashMap<String, Double> wordScores = this.calculateWordScores(keywords);
        LinkedHashMap<String, Double> keywordCandidates = this.getCandidateKeywordScores(keywords, wordScores);
        return this.sortHashMap(keywordCandidates);
    }
}

