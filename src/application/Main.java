package application;

import application.Model.PdfUtils;
import application.Model.PptUtils;
import application.Model.Types.PdfData;
import application.Model.Types.PptData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;


public class Main extends Application {
    // Change this to wtv you like most of this is testing :)
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("View/splashscreen.fxml"));
        PptData data = PptUtils.decode(new File("data/ppt/sample1.pptx"));
        PdfData pdfdata = PdfUtils.decode(new File("data/pdf/Chapter 14 - JavaFX with Scene Builder.pdf"));
        System.out.println(pdfdata.getText());
        ArrayList<BufferedImage> images = pdfdata.getImages();
        for (int i = 0; i < images.size(); i++) {
            ImageIO.write(images.get(i), "PNG", new File("data/images/" + i + ".png"));
        }
        Parent root = stageLoader.load();
        primaryStage.setTitle("Hackathon Submission");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
