package application.Controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Button btn;
    public static File file;
    public static boolean pdf = false;
    public static boolean ppt = false;
    public static boolean vtt = false;
    public static boolean docx = false;
    public static boolean mp4 = false;

    @FXML
    void btnOnClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documents",  "*.ppt", "*.docx", "*.pdf","*.vtt", "*.mp4");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fileChooser.showOpenDialog(btn.getScene().getWindow());
        if (file != null) {
            if (file.getName().matches(".+ppt")) {
                ppt = true;
            } else if (file.getName().matches(".+pdf")) {
                pdf = true;
            } else if (file.getName().matches(".+vtt")) {
                vtt = true;
            } else if (file.getName().matches(".+docx")) {
                docx = true;
            } else {
                mp4 = true;
            }
            Parent root = FXMLLoader.load(Main.class.getResource("View/main.fxml"));
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.setResizable(false);
    }
}
