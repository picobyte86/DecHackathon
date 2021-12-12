package application.Model.Types;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class DocxData {
    private ArrayList<TextGroup> text;
    private ArrayList<BufferedImage> images;
    private int wordCount;

    public DocxData(ArrayList<TextGroup> text, ArrayList<BufferedImage> images) {
        this.text = text;
        this.images = images;
    }

    public ArrayList<TextGroup> getTextGroup() {
        return text;
    }

    public ArrayList<String> getText() {
        ArrayList<String> ret = new ArrayList<String>();
        for (int i = 0; i < text.size(); i++) {
            ret.addAll(text.get(i).getWords());
        }
        return ret;
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

    public int getWordCount() {
        int ret = 0;
        for (TextGroup i : text) {
            ret += i.getWords().size();
        }
        return ret;
    }

    @Override
    public String toString() {
        return "DocxData{" +
                "text=" + text +
                ", images=" + images +
                ", wordCount=" + wordCount +
                '}';
    }
}
