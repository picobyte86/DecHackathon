package application.Model.Types;

import javafx.scene.media.Media;

import java.util.Date;

public class Mp4Data {
    private String name;
    private Date date;
    private Media video;

    public Mp4Data(String name, Date date, Media video) {
        this.name = name;
        this.date = date;
        this.video = video;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Media getVideo() {
        return video;
    }

    public void setVideo(Media video) {
        this.video = video;
    }

    @Override
    public String toString() {
        return "Mp4Data{" +
                "name='" + name + '\'' +
                ", date=" + date +
                ", video=" + video +
                '}';
    }
}
