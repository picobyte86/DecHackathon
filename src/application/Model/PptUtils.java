package application.Model;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.awt.image.BufferedImage;


import java.util.stream.Collectors;
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

public class PptUtils {
    public PptUtils() {

    }

    public static PptData decode(File file) throws IOException, ParserConfigurationException, SAXException {
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
        return data;
    }

    public static ArrayList<Result> search(PptData data) throws IOException, InterruptedException {
        ArrayList<Result> ret = new ArrayList<Result>();
        ret.addAll(searchSlides(data));
        ret.addAll(searchComments(data));
        return ret;
    }

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

    public static ArrayList<OnlineResource> searchEntry(String keyword, HttpClient client) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://scholar.google.com/scholar?q=" + keyword))
                .header("Content-Type", "text/html; charset=UTF-8")
                //.header("X-Requested-With","XMLHttpRequest")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.93 Safari/537.36")
                .GET()
                .build();
        HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.statusCode());
        System.out.println(response.body());
        org.jsoup.nodes.Document webPage = Jsoup.parse(response.body());
        Elements sources = webPage.getElementsByClass("gs_r gs_or gs_scl");
        ArrayList<OnlineResource> ret = new ArrayList<OnlineResource>();
        int sourceSize = sources.size();
        if (sources.size() > 5) {
            sourceSize = 5;
        }
        for (int i = 0; i < sourceSize; i++) {
            org.jsoup.nodes.Element source = sources.get(i).getElementsByClass("gs_ri")
                    .get(0);
            try {
                OnlineResource r = new OnlineResource(
                        source.getElementsByClass("gs_rt")
                                .get(0)
                                .getElementsByTag("a")
                                .get(0)
                                .attr("href"),
                        source.getElementsByClass("gs_a")
                                .get(0)
                                .text(),
                        source.getElementsByClass("gs_rs")
                                .get(0)
                                .text()
                );
                ret.add(r);
            } catch (IndexOutOfBoundsException e) {
                OnlineResource r = new OnlineResource("Not Found", "", "");
                ret.add(r);
            }
        }
        System.out.println(ret);
        return ret;
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
        Collections.addAll(ret, retArray);
        ArrayList<String> Cret = new ArrayList<String>();
        for (int i = 0; i < ret.size(); i++) {
            if (!(ret.get(i).equals("") || ret.get(i) == null || isNumeric(ret.get(i)))) {
                Cret.add(ret.get(i));
            }
        }
        return Cret;
    }

    private static boolean isNumeric(CharSequence sequence) {
        for (int i = 0; i < sequence.length(); i++) {
            if (!Character.isDigit(sequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
