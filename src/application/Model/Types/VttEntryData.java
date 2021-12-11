package application.Model.Types;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class VttEntryData {
    // Wrapper for 1 vtt entry
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss.SSS");
    private double confidence;
    private UUID uuid;
    private Date t1;
    private Date t2;
    private String subtitle;

    public VttEntryData(double confidence, UUID uuid, Date t1, Date t2, String subtitle) {
        this.confidence = confidence;
        this.uuid = uuid;
        this.t1 = t1;
        this.t2 = t2;
        this.subtitle = subtitle;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Date getT1() {
        return t1;
    }

    public void setT1(Date t1) {
        this.t1 = t1;
    }

    public Date getT2() {
        return t2;
    }

    public void setT2(Date t2) {
        this.t2 = t2;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    @Override
    public String toString() {
        return "VttEntryData{" +
                "confidence=" + confidence +
                ", uuid=" + uuid +
                ", t1=" + t1 +
                ", t2=" + t2 +
                ", subtitle='" + subtitle + '\'' +
                '}';
    }
}
