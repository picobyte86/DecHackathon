package application;

import application.Model.PptUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader stageLoader = new FXMLLoader(getClass().getResource("View/splashscreen.fxml"));
        PptUtils.decode(new File("data/ppt/sample1.pptx"));
        Parent root = stageLoader.load();
        primaryStage.setTitle("Hackathon Submission");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
