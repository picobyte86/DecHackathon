package application.Model.Types;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PptData {
    private ArrayList<TextGroup> slideTxt;
    private ArrayList<TextGroup> commentTxt;
    private ArrayList<BufferedImage> images;

    public PptData(ArrayList<TextGroup> slideTxt, ArrayList<TextGroup> commentTxt, ArrayList<BufferedImage> images) {
        this.slideTxt = slideTxt;
        this.commentTxt = commentTxt;
        this.images = images;
    }

    public ArrayList<BufferedImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }

    public ArrayList<TextGroup> getSlideTxt() {
        return slideTxt;
    }

    public void setSlideTxt(ArrayList<TextGroup> slideTxt) {
        this.slideTxt = slideTxt;
    }

    public ArrayList<TextGroup> getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(ArrayList<TextGroup> commentTxt) {
        this.commentTxt = commentTxt;
    }

    @Override
    public String toString() {
        return "PptData{" +
                "slideTxt=" + slideTxt.toString() +
                ", commentTxt=" + commentTxt.toString() +
                '}';
    }
}
