package application.Model;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;


public class PptUtils {
    public PptUtils() {

    }
    public static void decode(File file) throws IOException, ParserConfigurationException, SAXException {
        ZipFile zFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zFile.entries();
        File contentType = new File("data/ppt/sample1/[Content_Types].xml");
        OutputStream zOutStream;
        while (entries.hasMoreElements()) {
            ZipEntry zEntry = entries.nextElement();
            String[] zPath = zEntry.getName().split("/");
            Document zDoc;
            Element root;
            if (zEntry.isDirectory()) {
                System.out.println("---------------------Directory: " + zEntry.getName());
            } else if (zPath[0].equals("ppt") && zPath[1].equals("slides")) {
                zDoc = getDocument(zFile,zEntry);
                root = zDoc.getDocumentElement();
                NodeList a_t = root.getElementsByTagName("a:t");
                for (int i=0;i<a_t.getLength();i++) {
                    System.out.println(a_t.item(i).getTextContent());
                }

            } else if (zEntry.getName().equals("[Content_Types].xml")) {
                zDoc = getDocument(zFile,zEntry);
                root = zDoc.getDocumentElement();
                NodeList defaultNodes = root.getElementsByTagName("Default");
                for (int i=0;i<defaultNodes.getLength();i++) {
                    System.out.println((((Element)(defaultNodes.item(i))).getAttribute("ContentType")));
                }
                System.out.println("---------------------Found ContentType XML");
            } else {
                System.out.println(zEntry.getName());
            }
        }
        zFile.close();
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
}
