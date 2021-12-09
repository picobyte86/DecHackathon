package application.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class Controller {
    @FXML
    private Button btn;
    public File file;

    @FXML
    void btnOnClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Documents", "*.txt", "*.ppt", "*.pptx", "*.doc", "*.docx");
        fileChooser.getExtensionFilters().add(extFilter);
        Stage stage = new Stage();
        fileChooser.setTitle("Open File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        file = fileChooser.showOpenDialog(btn.getScene().getWindow());
        if (file != null) {

        }
    }
}
