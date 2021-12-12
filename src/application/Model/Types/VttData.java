package application.Model.Types;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VttData {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
    private ArrayList<VttEntryData> timeStampedEntries;
    private ArrayList<TextGroup> text;
    private double recognizability;
    private String language;
    private Date duration;

    public VttData(ArrayList<VttEntryData> entries, Date duration, double recognizability, String language, ArrayList<TextGroup> text) {
        this.timeStampedEntries = entries;
        this.recognizability = recognizability;
        this.language = language;
        this.text = text;
    }

    public ArrayList<TextGroup> getText() {
        return text;
    }

    public void setText(ArrayList<TextGroup> text) {
        this.text = text;
    }

    public int getSize() {
        return timeStampedEntries.size();
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public ArrayList<VttEntryData> getTimeStampedEntries() {
        return timeStampedEntries;
    }

    public void setTimeStampedEntries(ArrayList<VttEntryData> entries) {
        this.timeStampedEntries = entries;
    }

    public double getRecognizability() {
        return recognizability;
    }

    public void setRecognizability(double recognizability) {
        this.recognizability = recognizability;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getWordCount() {
        int ret = 0;
        for (TextGroup i : text) {
            ret += i.getWords().size();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "VttData{" +
                "entries=" + timeStampedEntries +
                ", recognizability=" + recognizability +
                ", language='" + language + '\'' +
                '}';
    }
}
