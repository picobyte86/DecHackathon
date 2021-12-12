package application.Model;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.awt.image.BufferedImage;


import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import application.Model.Types.OnlineResource;
import application.Model.Types.PptData;
import application.Model.Types.Result;
import application.Model.Types.TextGroup;
import libraries.rake.com.linguistic.rake.Rake;
import libraries.rake.com.linguistic.rake.RakeLanguages;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;

import static application.Model.Utils.*;
import static application.Model.WebScrap.searchEntry;

public class PptUtils {
    public static long time;
    public PptUtils() {

    }
    // Extracts Text and Images from PPT, text processed using rake
    public static PptData decode(File file) throws IOException, ParserConfigurationException, SAXException {
        long start = System.currentTimeMillis();
        ZipFile zFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zFile.entries();
        ArrayList<String> slideTxt = new ArrayList<String>();
        ArrayList<String> commentTxt = new ArrayList<String>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        Rake zRake = new Rake(RakeLanguages.ENGLISH);
        int slideCount = 0;
        int commentCount = 0;
        while (entries.hasMoreElements()) {
            ZipEntry zEntry = entries.nextElement();
            String[] zPath = zEntry.getName().split("/");
            Document zDoc;
            Element root;
            if (zEntry.isDirectory()) {
            } else if (zPath[0].equals("ppt") && zPath[1].equals("media")) {
                String ext = zPath[2].split("\\.")[1];
                if (ext.matches("jpg|gif|png|bmp|tif|webp")) {
                    BufferedImage zImage = getImage(zFile, zEntry);
                    images.add(zImage);
                }
            } else if (zPath[0].equals("ppt") && zPath[1].equals("slides")) {
                if (!zPath[2].equals("_rels")) {
                    slideCount += 1;
                    //slideTxt.add("Slide " + slideCount);
                    zDoc = getDocument(zFile, zEntry);
                    root = zDoc.getDocumentElement();
                    NodeList a_t = root.getElementsByTagName("a:t");
                    for (int i = 0; i < a_t.getLength(); i++) {
                        slideTxt.add(a_t.item(i).getTextContent());
                    }
                }
            } else if (zPath[0].equals("ppt") && zPath[1].equals("notesSlides")) {
                if (!zPath[2].equals("_rels")) {
                    commentCount += 1;
                    //commentTxt.add("Slide " + commentCount);
                    zDoc = getDocument(zFile, zEntry);
                    root = zDoc.getDocumentElement();
                    NodeList a_t = root.getElementsByTagName("a:t");
                    for (int i = 0; i < a_t.getLength(); i++) {
                        commentTxt.add(a_t.item(i).getTextContent());
                    }
                }

            } else if (zEntry.getName().equals("[Content_Types].xml")) {
                zDoc = getDocument(zFile, zEntry);
                root = zDoc.getDocumentElement();
                NodeList defaultNodes = root.getElementsByTagName("Default");
            }
        }
        ArrayList<String> CslideTxt = cleanText(String.join(" ", slideTxt));
        ArrayList<String> CcommentTxt = cleanText(String.join(" ", commentTxt));
        LinkedHashMap<String, Double> KslideTxt = zRake.getKeywordsFromText(String.join(" ", CslideTxt));
        LinkedHashMap<String, Double> KcommentTxt = zRake.getKeywordsFromText(String.join(" ", CcommentTxt));
        ArrayList<TextGroup> retSlideTxt = new ArrayList<TextGroup>();

        ArrayList<TextGroup> retCommentTxt = new ArrayList<TextGroup>();
        KslideTxt.forEach((txt, i) -> {
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp, txt.split("\\s+"));
            TextGroup group = new TextGroup(temp, i);
            retSlideTxt.add(group);
        });
        KcommentTxt.forEach((txt, i) -> {
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp, txt.split("\\s+"));
            TextGroup group = new TextGroup(temp, i);
            retCommentTxt.add(group);
        });
        PptData data = new PptData(retSlideTxt, retCommentTxt, images);
        zFile.close();
        long end = System.currentTimeMillis();
        time = end - start;
        return data;
    }

    // Searches internet for keywords from powerpoint
    public static ArrayList<Result> search(PptData data) throws IOException, InterruptedException {
        ArrayList<Result> ret = new ArrayList<Result>();
        ret.addAll(searchSlides(data));
        ret.addAll(searchComments(data));
        return ret;
    }

    // Searches internet for keywords from powerpoint slide text
    public static ArrayList<Result> searchSlides(PptData data) throws IOException, InterruptedException {
        ArrayList<Result> ret = new ArrayList<Result>();
        HttpClient client = HttpClient.newBuilder()
                .build();
        ArrayList<String> slideTxt = data.getSlideTxt();
        for (int i = 0; i < slideTxt.size(); i++) {
            Result r = new Result(slideTxt.get(i), searchEntry(slideTxt.get(i), client));
            ret.add(r);
        }
        return ret;
    }

    // Searches internet for keywords from powerpoint comments
    public static ArrayList<Result> searchComments(PptData data) throws IOException, InterruptedException {
        ArrayList<Result> ret = new ArrayList<Result>();
        HttpClient client = HttpClient.newBuilder()
                .build();
        ArrayList<String> commentTxt = data.getCommentTxt();
        for (int i = 0; i < commentTxt.size(); i++) {
            Result r = new Result(commentTxt.get(i), searchEntry(commentTxt.get(i), client));
            ret.add(r);
        }
        return ret;
    }
}
