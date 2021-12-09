package application.Model;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PptUtils {
    public PptUtils() {

    }
    public static void decode(File file) throws IOException {
        ZipFile zFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zFile.entries();
        File contentType = new File("data/ppt/sample1/[Content_Types].xml");
        InputStream zStream;
        OutputStream zOutStream;
        while (entries.hasMoreElements()) {
            ZipEntry zEntry = entries.nextElement();
            if (zEntry.isDirectory()) {
                System.out.println("---------------------Directory: " + zEntry.getName());
            } else if (zEntry.getName().equals("[Content_Types].xml")) {
                zStream = zFile.getInputStream(zEntry);
                byte[] zBuffer = zStream.readAllBytes();
                zOutStream = new FileOutputStream(contentType);
                zOutStream.write(zBuffer);
                zStream.close();
                System.out.println("---------------------Found ContentType XML");
            } else {
                System.out.println(zEntry.getName());
            }
        }
        zFile.close();
    }
}
