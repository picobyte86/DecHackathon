package application.Controller;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Controller {
    @FXML
    private Button btn;
    public static File file;
    public static boolean pdf = false;
    public static boolean ppt = false;

    @FXML
    void btnOnClick(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documents", "*.txt", "*.ppt", "*.pptx", "*.doc", "*.docx", "*.pdf");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fileChooser.showOpenDialog(btn.getScene().getWindow());
        if (file != null) {
            if (file.getName().matches("(.+ppt) | (.+pptx)")) {
                ppt = true;
            } else if (file.getName().matches(".+pdf")) {
                pdf = true;
            }
            Parent root = FXMLLoader.load(Main.class.getResource("View/main.fxml"));
            Stage stage = (Stage) btn.getScene().getWindow();
            stage.setScene(new Scene(root));
        }
    }
}
