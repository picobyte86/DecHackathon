package application;

import application.Model.PptUtils;
import application.Model.Types.PptData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("View/splashscreen.fxml"));
        PptData data = PptUtils.decode(new File("data/ppt/sample1.pptx"));
        ArrayList<String> slideTxt = data.getSlideTxt();
        ArrayList<String> commentTxt = data.getCommentTxt();
        System.out.println(slideTxt.toString());
        System.out.println(commentTxt.toString());
        Parent root = stageLoader.load();
        primaryStage.setTitle("Hackathon Submission");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
