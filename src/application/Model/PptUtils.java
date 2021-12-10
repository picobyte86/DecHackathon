package application.Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.awt.image.BufferedImage;

import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import application.Model.Types.PptData;
import application.Model.Types.TextGroup;
import libraries.rake.com.linguistic.rake.Rake;
import libraries.rake.com.linguistic.rake.RakeLanguages;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.*;


public class PptUtils {
    public PptUtils() {

    }

    public static PptData decode(File file) throws IOException, ParserConfigurationException, SAXException {
        ZipFile zFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zFile.entries();
        File contentType = new File("data/ppt/sample1/[Content_Types].xml");
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
                System.out.println("---------------------Directory: " + zEntry.getName());
            } else if (zPath[0].equals("ppt") && zPath[1].equals("media")) {
                String ext = zPath[2].split("\\.")[1];
                if (ext.matches("jpg|gif|png|bmp|tif|webp")) {
                    System.out.println("&&&&&&" + ext);
                    BufferedImage zImage = getImage(zFile, zEntry);
                    images.add(zImage);
                    //ImageIO.write(zImage, ext, new File("data/ppt/sample1/ppt/media/new" + zPath[2]));
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
                for (int i = 0; i < defaultNodes.getLength(); i++) {
                    System.out.println((((Element) (defaultNodes.item(i))).getAttribute("ContentType")));
                }
                System.out.println("---------------------Found ContentType XML");
            } else {
                System.out.println(zEntry.getName());
            }
        }
        ArrayList<String> CslideTxt = cleanText(String.join(" ", slideTxt));
        ArrayList<String> CcommentTxt = cleanText(String.join(" ", commentTxt));
        System.out.println(CslideTxt);
        System.out.println(CcommentTxt);
        LinkedHashMap<String, Double> KslideTxt = zRake.getKeywordsFromText(String.join(" ", CslideTxt));
        LinkedHashMap<String, Double> KcommentTxt = zRake.getKeywordsFromText(String.join(" ", CcommentTxt));
        ArrayList<TextGroup> retSlideTxt = new ArrayList<TextGroup>();

        ArrayList<TextGroup> retCommentTxt = new ArrayList<TextGroup>();
        KslideTxt.forEach((txt,i)->{
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp,txt.split("\\s+"));
            TextGroup group = new TextGroup(temp,i);
            retSlideTxt.add(group);
        });
        KcommentTxt.forEach((txt,i)->{
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp,txt.split("\\s+"));
            TextGroup group = new TextGroup(temp,i);
            retCommentTxt.add(group);
        });
        PptData data = new PptData(retSlideTxt, retCommentTxt, images);
        zFile.close();
        return data;
    }

    private static Document getDocument(ZipFile file, ZipEntry entry) throws IOException, ParserConfigurationException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream zStream;
        zStream = file.getInputStream(entry);
        byte[] zBuffer = zStream.readAllBytes();
        ByteArrayInputStream zInputStream = new ByteArrayInputStream(zBuffer);
        zStream.close();
        return builder.parse(zInputStream);
    }

    private static BufferedImage getImage(ZipFile file, ZipEntry entry) throws IOException {
        InputStream zStream;
        zStream = file.getInputStream(entry);
        byte[] zBuffer = zStream.readAllBytes();
        ByteArrayInputStream zInputStream = new ByteArrayInputStream(zBuffer);
        BufferedImage zImage = ImageIO.read(zInputStream);
        zStream.close();
        return zImage;
    }
    private static ArrayList<String> cleanText(String text) {
        //[^abcdeABCDE1234567890-]
        String[] retArray = text.split("[^\\w-]");
        ArrayList<String> ret = new ArrayList<String>();
        Collections.addAll(ret,retArray);
        ArrayList<String> Cret = new ArrayList<String>();
        for (int i=0;i< ret.size();i++) {
            if (!(ret.get(i).equals("") || ret.get(i)==null || isNumeric(ret.get(i)))) {
                Cret.add(ret.get(i));
            }
        }
        return Cret;
    }
    private static boolean isNumeric(CharSequence sequence) {
        for (int i=0;i<sequence.length();i++) {
            if (!Character.isDigit(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
