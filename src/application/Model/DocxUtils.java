package application.Model;

import application.Model.Types.DocxData;
import application.Model.Types.PptData;
import application.Model.Types.Result;
import application.Model.Types.TextGroup;
import libraries.rake.com.linguistic.rake.Rake;
import libraries.rake.com.linguistic.rake.RakeLanguages;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static application.Model.Utils.*;
import static application.Model.WebScrap.searchEntry;

public class DocxUtils {
    public static long time;

    public DocxUtils() {

    }

    // Extracts Text and Images from word, text processed using rake
    public static DocxData decode(File file) throws IOException, ParserConfigurationException, SAXException {
        long start = System.currentTimeMillis();
        ZipFile zFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zFile.entries();
        ArrayList<String> text = new ArrayList<String>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
        Rake zRake = new Rake(RakeLanguages.ENGLISH);
        int directories = 0;
        while (entries.hasMoreElements()) {
            ZipEntry zEntry = entries.nextElement();
            String[] zPath = zEntry.getName().split("/");
            Document zDoc;
            Element root;
            if (zEntry.isDirectory()) {
                directories += 1;
            } else if (zPath[0].equals("word") && zPath[1].equals("media")) {
                String ext = zPath[2].split("\\.")[1];
                if (ext.matches("jpg|gif|png|bmp|tif|webp")) {
                    BufferedImage zImage = getImage(zFile, zEntry);
                    images.add(zImage);
                }
            } else if (zPath[0].equals("word") && zPath[1].equals("document.xml")) {
                zDoc = getDocument(zFile, zEntry);
                root = zDoc.getDocumentElement();
                NodeList a_t = root.getElementsByTagName("w:t");
                for (int i = 0; i < a_t.getLength(); i++) {
                    text.add(a_t.item(i).getTextContent());
                }
            } else if (zEntry.getName().equals("[Content_Types].xml")) {
                zDoc = getDocument(zFile, zEntry);
                root = zDoc.getDocumentElement();
                NodeList defaultNodes = root.getElementsByTagName("Default");
                for (int i = 0; i < defaultNodes.getLength(); i++) {
                    System.out.println((((Element) (defaultNodes.item(i))).getAttribute("ContentType")));
                }
                System.out.println("---------------------Found ContentType XML");
            } else {
                System.out.println(zEntry.getName());
            }
        }
        ArrayList<String> Ctext = cleanText(String.join(" ", text));
        LinkedHashMap<String, Double> Ktext = zRake.getKeywordsFromText(String.join(" ", Ctext));
        ArrayList<TextGroup> retText = new ArrayList<>();

        Ktext.forEach((txt, i) -> {
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp, txt.split("\\s+"));
            TextGroup group = new TextGroup(temp, i);
            retText.add(group);
        });
        DocxData data = new DocxData(retText, images);
        zFile.close();
        long end = System.currentTimeMillis();
        time = end - start;
        return data;
    }

    // Searches internet for keywords from word
    public static ArrayList<Result> search(DocxData data) throws IOException, InterruptedException {
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
