package application;

import application.Model.*;
import application.Model.Types.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.http.HttpClient;
import java.util.ArrayList;


public class Main extends Application {
    // Change this to wtv you like most of this is testing :)
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("View/splashscreen.fxml"));
        //DocxData data = DocxUtils.decode(new File("data/word/sample2.docx"));
        //ArrayList<Result> r = DocxUtils.search(data);
        //for (Result i : r) {
        //    i.save(new File("data/results/result - " + i.getKeyword() + ".txt"));
        //}
        //PdfData pdfdata = PdfUtils.decode(new File("data/pdf/Chapter 14 - JavaFX with Scene Builder.pdf"));
        //System.out.println(pdfdata.getText());
        //ArrayList<BufferedImage> images = pdfdata.getImages();
        //for (int i = 0; i < images.size(); i++) {
        //    ImageIO.write(images.get(i), "PNG", new File("data/images/" + i + ".png"));
        //}
        //DocxData data = DocxUtils.decode(new File("data/word/sample1.docx"));
        //System.out.println(data.getTextGroup());
        //data.save(new File(""), new File(""));
        //VttData data1 = VttUtils.decode(new File("data/VTT/sample1.vtt"));
        //System.out.println(data1.getText());
        PptData a = PptUtils.decode(new File("data/ppt/sample1.pptx"));
        System.out.println(a.getRawSlideTxt());
        Parent root = stageLoader.load();
        primaryStage.setTitle("Hackathon Submission");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
