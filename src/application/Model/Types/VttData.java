package application.Model.Types;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VttData {

    public static SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SSSSSSS");
    private ArrayList<VttEntryData> entries;
    private double recognizability;
    private String language;
    private Date duration;

    public VttData(ArrayList<VttEntryData> entries,Date duration, double recognizability, String language) {
        this.entries = entries;
        this.recognizability = recognizability;
        this.language = language;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public ArrayList<VttEntryData> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<VttEntryData> entries) {
        this.entries = entries;
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

    @Override
    public String toString() {
        return "VttData{" +
                "entries=" + entries +
                ", recognizability=" + recognizability +
                ", language='" + language + '\'' +
                '}';
    }
}
