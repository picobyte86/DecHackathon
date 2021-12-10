package application.Model;

import application.Model.Types.PdfData;

import application.Model.Types.TextGroup;
import libraries.rake.com.linguistic.rake.Rake;
import libraries.rake.com.linguistic.rake.RakeLanguages;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.graphics.PDXObject;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.text.PDFTextStripper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class PdfUtils {
    // Extracts Text and Images from PDF, text processed using rake
    public static PdfData decode(File file) throws IOException {
        Rake rake = new Rake(RakeLanguages.ENGLISH);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        ArrayList<TextGroup> retText = new ArrayList<TextGroup>();
        ArrayList<BufferedImage> retImages = new ArrayList<BufferedImage>();
        // text
        PDDocument doc = PDDocument.load(file);
        String text = String.join(" ", Utils.cleanText(pdfStripper.getText(doc)));
        LinkedHashMap<String, Double> rText = rake.getKeywordsFromText(text);
        rText.forEach((txt, i) -> {
            ArrayList<String> temp = new ArrayList<String>();
            Collections.addAll(temp, txt.split("\\s+"));
            TextGroup group = new TextGroup(temp, i);
            retText.add(group);
        });
        // images
        doc.getPages().forEach((page) -> {
            page.getResources().getXObjectNames().forEach((a) -> {
                try {
                    PDXObject obj = page.getResources().getXObject(a);
                    if (obj instanceof PDImageXObject) {
                        retImages.add(((PDImageXObject) obj).getImage());
                    }
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            });
        });
        doc.close();

        PdfData ret = new PdfData(retText, retImages);
        return ret;
    }
}
