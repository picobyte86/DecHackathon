package application.Model.Types;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PptData {
    private ArrayList<String> slideTxt;
    private ArrayList<String> commentTxt;
    private ArrayList<BufferedImage> images;

    public PptData(ArrayList<String> slideTxt, ArrayList<String> commentTxt, ArrayList<BufferedImage> images) {
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

    public ArrayList<String> getSlideTxt() {
        return slideTxt;
    }

    public void setSlideTxt(ArrayList<String> slideTxt) {
        this.slideTxt = slideTxt;
    }

    public ArrayList<String> getCommentTxt() {
        return commentTxt;
    }

    public void setCommentTxt(ArrayList<String> commentTxt) {
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
