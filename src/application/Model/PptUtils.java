package application.Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.awt.image.BufferedImage;

import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import application.Model.Types.PptData;
import javafx.scene.image.Image;
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
        OutputStream zOutStream;
        ArrayList<String> slideTxt = new ArrayList<String>();
        ArrayList<String> commentTxt = new ArrayList<String>();
        ArrayList<BufferedImage> images = new ArrayList<BufferedImage>();
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
                    ImageIO.write(zImage, ext, new File("data/ppt/sample1/ppt/media/new" + zPath[2]));
                }
            } else if (zPath[0].equals("ppt") && zPath[1].equals("slides")) {
                if (!zPath[2].equals("_rels")) {
                    slideCount += 1;
                    slideTxt.add("Slide " + slideCount);
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
                    commentTxt.add("Slide " + commentCount);
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
        PptData data = new PptData(slideTxt, commentTxt);
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
}
