package application.Controller;

import application.Main;
import javafx.animation.SequentialTransition;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class SplashController implements Initializable {
    @FXML
    private Label label;

    @FXML
    private ProgressBar progbar;

    private final SequentialTransition progress = new SequentialTransition();

    private static ObservableList<String> texts = FXCollections.observableArrayList("Looking at files...",
            "Microsoft PPT loaded...", "Scanner is alive", "Loading Buttons...", "Preparing APP...",
            "The Sun is Hot...", "Coding Main.java...", "Crying after finishing Code...", "Trying to understand Words", "Experimenting with Java", "Bug Testing Code", "Generating Poor Code",
            "Catching all bugs...", "Gotta Catch Them All...",
            "Translating into Python...",
            "Creating TextFields...", "Drinking Something...", "Looking At Notes...",
            "Creating Logo...", "Playing Minecraft...", "Loading JFoenix...", "Creating GUI..." , "Completed.");






    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        progress.getChildren().add(new Transition() {{
            setCycleDuration(Duration.millis(2000));
        }

            @Override
            protected void interpolate(double frac) {
                label.setText(texts.get((int) (frac * (texts.size() - 1))));
                progbar.setProgress(frac);
                if(frac==1){
                    ((Stage)label.getScene().getWindow()).close();
                    try {
                        Stage stage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/sample.fxml"));
                        Scene scene = new Scene(fxmlLoader.load());
                        stage.setTitle("MSAnalysis");
                        stage.setScene(scene);
                        stage.show();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        progress.play();
    }

}
