package application.Controller;

import application.Main;
import application.Model.PdfUtils;
import application.Model.PptUtils;
import application.Model.Types.PdfData;
import application.Model.Types.PptData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private TextArea ta1;

    @FXML
    private TextArea ta2;

    @FXML
    private void backBtnOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("View/sample.fxml"));
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.initStyle(StageStyle.UNDECORATED);
        if (Controller.pdf) {
            try {
                PdfData pdf = PdfUtils.decode(Controller.file);
                ta1.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (Controller.ppt) {
            try {
                PptData ppt = PptUtils.decode(Controller.file);
            } catch (IOException | SAXException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }else {

        }
    }
}
