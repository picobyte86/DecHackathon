package application.Model;

import application.Model.Types.TextGroup;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Utils {
    // Remove non word and newline chars
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

    // helper method for getting xml file from archive
    public static Document getDocument(ZipFile file, ZipEntry entry) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream zStream;
        zStream = file.getInputStream(entry);
        byte[] zBuffer = zStream.readAllBytes();
        ByteArrayInputStream zInputStream = new ByteArrayInputStream(zBuffer);
        zStream.close();
        return builder.parse(zInputStream);
    }

    // above but image file
    public static BufferedImage getImage(ZipFile file, ZipEntry entry) throws IOException {
        InputStream zStream;
        zStream = file.getInputStream(entry);
        byte[] zBuffer = zStream.readAllBytes();
        ByteArrayInputStream zInputStream = new ByteArrayInputStream(zBuffer);
        BufferedImage zImage = ImageIO.read(zInputStream);
        zStream.close();
        return zImage;
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

    public static ArrayList<TextGroup> bugFix(ArrayList<TextGroup> text) {
        ArrayList<TextGroup> ret = new ArrayList<TextGroup>();
        for (TextGroup i : text) {
            ArrayList<String> retText = new ArrayList<String>();
            for (String j : i.getWords()) {
                retText.add(j);
            }
            TextGroup retGroup = new TextGroup(retText, i.getWeight());
            ret.add(retGroup);
        }
        for (int i = 0; i < ret.size(); i++) {
            for (int j = 0; j < ret.size(); j++) {
                if (i != j) {
                    for (String k : ret.get(j).getWords()) {
                        while (ret.get(i).getWords().contains(k)) {
                            ret.get(i).getWords().remove(k);
                        }
                    }
                }
            }
        }
        for (TextGroup i : ret) {
            ArrayList<String> temp = new ArrayList<String>();
            for (String j : i.getWords()) {
                if (!temp.contains(j)) {
                    temp.add(j);
                }
            }
            System.out.println(temp);
            i.getWords().clear();
            i.getWords().addAll(temp);
        }
        return ret;
    }
}
