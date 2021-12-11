package application.Model;

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
}
