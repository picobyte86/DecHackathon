package application.Model;

import application.Model.Types.*;
import libraries.rake.com.linguistic.rake.Rake;
import libraries.rake.com.linguistic.rake.RakeLanguages;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.http.HttpClient;
import java.text.ParseException;
import java.util.*;

import static application.Model.Utils.bugFix;
import static application.Model.WebScrap.searchEntry;

public class VttUtils {
    public static VttData decode(File file) throws FileNotFoundException, ParseException {
        Scanner sc = new Scanner(file);
        ArrayList<VttEntryData> data = new ArrayList<VttEntryData>();
        Rake rake = new Rake(RakeLanguages.ENGLISH);
        sc.nextLine();
        sc.nextLine();
        Date duration = VttData.dateFormat.parse(parseNoteValue(sc.nextLine(), "duration"));
        sc.nextLine();
        double recognizability = Double.parseDouble(parseNoteValue(sc.nextLine(), "recognizability"));
        sc.nextLine();
        String language = parseNoteValue(sc.nextLine(), "language");
        sc.nextLine();
        ArrayList<String> rakeText = new ArrayList<>();
        while (sc.hasNextLine()) {
            double confidence = Double.parseDouble(parseNoteValue(sc.nextLine(), "Confidence"));
            sc.nextLine();
            UUID uuid = UUID.fromString(sc.nextLine());
            String[] temp = sc.nextLine().split(" --> ");
            Date t1 = VttEntryData.dateFormat.parse(temp[0]);
            Date t2 = VttEntryData.dateFormat.parse(temp[1]);
            String subtitle = sc.nextLine();
            sc.nextLine();
            VttEntryData entry = new VttEntryData(confidence, uuid, t1, t2, subtitle);
            rakeText.addAll(Utils.cleanText(subtitle));
            data.add(entry);
        }
        LinkedHashMap<String, Double> Ktext = rake.getKeywordsFromText(String.join(" ", rakeText));
        ArrayList<TextGroup> retText = new ArrayList<>();

        Ktext.forEach((txt, i) -> {
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp, txt.split("\\s+"));
            TextGroup group = new TextGroup(temp, i);
            retText.add(group);
        });

        VttData ret = new VttData(data, duration, recognizability, language, bugFix(retText));
        return ret;
    }

    public static String parseNoteKey(String note) {
        if (note.startsWith("NOTE ")) {
            return note.substring(5).split(":")[0];
        } else {
            return "";
        }
    }

    public static String parseNoteValue(String note, String item) {
        if (note.startsWith("NOTE ")) {
            JSONObject obj = new JSONObject("{" + note.substring(5) + "}");
            return String.valueOf(obj.get(item));
        } else {
            return "";
        }
    }

    public static HashMap<String, String> parseNote(String note) {
        HashMap<String, String> ret = new HashMap<>();
        if (note.startsWith("NOTE ")) {
            String[] temp = note.substring(5).split(":");
            ret.put(temp[0], temp[1]);
            return ret;
        } else {
            return null;
        }
    }

    public static HashMap<String, String> parseNote(ArrayList<String> notes) {
        HashMap<String, String> ret = new HashMap<>();
        for (String i : notes) {
            if (i.startsWith("NOTE ")) {
                String[] temp = i.substring(5).split(":");
                ret.put(temp[0], temp[1]);
            }
        }
        return ret;
    }
    public static ArrayList<Result> search(VttData data) throws IOException, InterruptedException {
        ArrayList<Result> ret = new ArrayList<Result>();
        HttpClient client = HttpClient.newBuilder()
                .build();
        ArrayList<String> text = data.getText();
        for (int i = 0; i < text.size(); i++) {
            Result r = new Result(text.get(i), searchEntry(text.get(i), client));
            ret.add(r);
        }
        return ret;
    }
}
