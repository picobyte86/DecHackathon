package application.Model.Types;

import java.util.ArrayList;

public class PptData {
    private ArrayList<String> slideTxt;
    private ArrayList<String> commentTxt;

    public PptData(ArrayList<String> slideTxt, ArrayList<String> commentTxt) {
        this.slideTxt = slideTxt;
        this.commentTxt = commentTxt;
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
