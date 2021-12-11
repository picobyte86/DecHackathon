package application.Model.Types;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
/*
* Wrapper for processed pdf data
 */
public class PdfData {
    private ArrayList<TextGroup> text;
    private ArrayList<BufferedImage> images;

    public PdfData(ArrayList<TextGroup> text, ArrayList<BufferedImage> images) {
        this.text = text;
        this.images = images;
    }
    public int getWordCount() {
        int ret = 0;
        for (TextGroup i : text) {
            ret += i.getWords().size();
        }
        return ret;
    }
    public ArrayList<TextGroup> getText() {
        return text;
    }

    public void setText(ArrayList<TextGroup> text) {
        this.text = text;
    }

    public ArrayList<BufferedImage> getImages() {
        return images;
    }

    public void setImages(ArrayList<BufferedImage> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "PdfData{" +
                "text=" + text +
                ", images=" + images +
                '}';
    }
}
