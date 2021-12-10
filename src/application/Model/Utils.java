package application.Model;

import java.util.ArrayList;
import java.util.Collections;

public class Utils {
    // Remove non word and newline chars
    //todo filter numbers / better detection of words associated with number
    public static ArrayList<String> cleanText(String text) {
        String[] retArray = text.split("[^\\w-]");
        ArrayList<String> ret = new ArrayList<String>();
        Collections.addAll(ret, retArray);
        ArrayList<String> Cret = new ArrayList<String>();
        for (int i = 0; i < ret.size(); i++) {
            if (!(ret.get(i).equals("") || ret.get(i) == null || isNumeric(ret.get(i)))) {
                Cret.add(ret.get(i));
            }
        }
        return Cret;
    }

    // jdk doesn't have this smh
    public static boolean isNumeric(CharSequence sequence) {
        for (int i = 0; i < sequence.length(); i++) {
            if (!Character.isDigit(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
