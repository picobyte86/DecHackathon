package application.Controller;

import application.Main;
import application.Model.DocxUtils;
import application.Model.PdfUtils;
import application.Model.PptUtils;
import application.Model.Types.*;
import application.Model.VttUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Button backBtn;

    @FXML
    private AnchorPane ap1;

    @FXML
    private AnchorPane ap2;


    @FXML
    private TextArea ta1;

    @FXML
    private TextArea ta2;

    File file;

    @FXML
    private void backBtnOnClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Main.class.getResource("View/sample.fxml"));
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        file = Controller.file;
        if (Controller.pdf) {
            try {
                PdfData pdf = PdfUtils.decode(file);
                ta1.setText("Words Processed: " + pdf.getWordCount() + "\n" + "Time taken: " + PdfUtils.time);
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
                for (TextGroup t : pdf.getTextEntries()) {
                    for (String s : t.getWords()) {
                        list.add(new PieChart.Data(s, t.getWeight()));
                    }
                }
                PieChart pieChart = new PieChart(list);
                pieChart.setLayoutX(-100);
                pieChart.setLayoutY(-60);
                pieChart.setScaleX(0.75);
                pieChart.setScaleY(0.75);
                pieChart.setLegendVisible(false);
                pieChart.setLabelsVisible(true);

                ap2.getChildren().addAll(pieChart);

                ArrayList<Result> result = PdfUtils.search(pdf);
                int count  = 0;
                for (Result r: result) {
                    Hyperlink h = new Hyperlink(r.getKeyword());
                    h.setLayoutX(20);
                    h.setLayoutY(30 + 10 * count);
                    count += 1;
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            Stage primaryStage = new Stage();
                            ScrollPane sp3 = new ScrollPane();
                            double count2 = 10;
                            for (OnlineResource or :r.getResults()) {
                                Label l = new Label(or.getTitle() + ": " + or.getHyperlink());
                                sp3.getChildrenUnmodifiable().add(l);
                                l.setLayoutY(count2);
                                count2 = 30 + l.getLayoutY() + l.getPrefWidth();
                            }
                            Scene scene = new Scene(sp3);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                        }
                    });
                    ap1.getChildren().add(h);
                }
                for (Result i : result) {
                    i.save(new File("data/results/" + file.getName() + " - " + i.getKeyword() + ".txt"));
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (Controller.ppt) {
            try {
                PptData ppt = PptUtils.decode(file);
                ta1.setText("Words Processed: " + ppt.getWordCount() + "\n" + "Time taken: " + PptUtils.time);
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
                for (TextGroup t : ppt.getSlideTxtGroup()) {
                    for (String s : t.getWords()) {
                        list.add(new PieChart.Data(s, t.getWeight()));
                    }
                }
                PieChart pieChart = new PieChart(list);
                pieChart.setLayoutX(-100);
                pieChart.setLayoutY(-60);
                pieChart.setScaleX(0.75);
                pieChart.setScaleY(0.75);
                pieChart.setLegendVisible(false);
                pieChart.setLabelsVisible(true);

                ObservableList<PieChart.Data> list2 = FXCollections.observableArrayList();
                for (TextGroup t : ppt.getCommentTxtGroup()) {
                    for (String s : t.getWords()) {
                        list2.add(new PieChart.Data(s, t.getWeight()));
                    }
                }
                PieChart pieChart2 = new PieChart(list2);
                pieChart2.setLayoutX(-100);
                pieChart2.setLayoutY(200);
                pieChart2.setScaleX(0.75);
                pieChart2.setScaleY(0.75);
                pieChart2.setLegendVisible(false);
                pieChart2.setLabelsVisible(true);
                ap2.getChildren().addAll(pieChart, pieChart2);

                ArrayList<Result> result = PptUtils.search(ppt);
                int count  = 0;
                for (Result r: result) {
                    Hyperlink h = new Hyperlink(r.getKeyword());
                    h.setLayoutX(20);
                    h.setLayoutY(30 + 10 * count);
                    count += 1;
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            Stage primaryStage = new Stage();
                            ScrollPane sp3 = new ScrollPane();
                            double count2 = 10;
                            for (OnlineResource or :r.getResults()) {
                                Label l = new Label(or.getTitle() + ": " + or.getHyperlink());
                                sp3.getChildrenUnmodifiable().add(l);
                                l.setLayoutY(count2);
                                count2 = 30 + l.getLayoutY() + l.getPrefWidth();
                            }
                            Scene scene = new Scene(sp3);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                        }
                    });
                    ap1.getChildren().add(h);
                }
                for (Result i : result) {
                    i.save(new File("data/results/" + file.getName() + " - " + i.getKeyword() + ".txt"));
                }
            } catch (IOException | SAXException | ParserConfigurationException | InterruptedException e) {
                e.printStackTrace();
            }
        } else if (Controller.vtt) {
            try {
                VttData vtt = VttUtils.decode(file);

                ta1.setText("Words Processed: " + vtt.getWordCount() + "\n" + "Time taken: " + vtt.getDuration());
                ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
                for (TextGroup t : vtt.getTextEntries()) {
                    for (String s : t.getWords()) {
                        list.add(new PieChart.Data(s, t.getWeight()));
                    }
                }
                PieChart pieChart = new PieChart(list);
                pieChart.setLayoutX(-100);
                pieChart.setLayoutY(-60);
                pieChart.setScaleX(0.75);
                pieChart.setScaleY(0.75);
                pieChart.setLegendVisible(false);
                pieChart.setLabelsVisible(true);
                pieChart.setLayoutX(0);

                ArrayList<Result> result = VttUtils.search(vtt);
                int count  = 0;
                for (Result r: result) {
                    Hyperlink h = new Hyperlink(r.getKeyword());
                    h.setLayoutX(20);
                    h.setLayoutY(30 + 10 * count);
                    count += 1;
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            Stage primaryStage = new Stage();
                            ScrollPane sp3 = new ScrollPane();
                            double count2 = 10;
                            for (OnlineResource or :r.getResults()) {
                                Label l = new Label(or.getTitle() + ": " + or.getHyperlink());
                                sp3.getChildrenUnmodifiable().add(l);
                                l.setLayoutY(count2);
                                count2 = 30 + l.getLayoutY() + l.getPrefWidth();
                            }
                            Scene scene = new Scene(sp3);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                        }
                    });
                    ap1.getChildren().add(h);
                }
                for (Result i : result) {
                    i.save(new File("data/results/" + file.getName() + " - " + i.getKeyword() + ".txt"));
                }
            } catch (FileNotFoundException | ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (Controller.mp4) {
            //Mp4Data mp4 = Mp4Utils.decode(file);
            //ta1.setText("Words Processed: " + mp4.getWordCount() + "\n" + "Time taken: " + Mp4Utils.time);
        } else {
            try {
                DocxData docx = DocxUtils.decode(file);
                ta1.setText("Words Processed: " + docx.getWordCount() + "\n" + "Time taken: " + DocxUtils.time);

                ObservableList<PieChart.Data> list = FXCollections.observableArrayList();
                for (TextGroup t : docx.getTextGroup()) {
                    for (String s : t.getWords()) {
                        list.add(new PieChart.Data(s, t.getWeight()));
                    }
                }
                PieChart pieChart = new PieChart(list);
                pieChart.setLayoutX(-100);
                pieChart.setLayoutY(-60);
                pieChart.setScaleX(0.75);
                pieChart.setScaleY(0.75);
                pieChart.setLegendVisible(false);
                pieChart.setLabelsVisible(true);

                ArrayList<Result> result = DocxUtils.search(docx);
                int count  = 0;
                for (Result r: result) {
                    Hyperlink h = new Hyperlink(r.getKeyword());
                    h.setLayoutX(20);
                    h.setLayoutY(30 + 10 * count);
                    count += 1;
                    h.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            Stage primaryStage = new Stage();
                            ScrollPane sp3 = new ScrollPane();
                            double count2 = 10;
                            for (OnlineResource or :r.getResults()) {
                                Label l = new Label(or.getTitle() + ": " + or.getHyperlink());
                                sp3.getChildrenUnmodifiable().add(l);
                                l.setLayoutY(count2);
                                count2 = 30 + l.getLayoutY() + l.getPrefWidth();
                            }
                            Scene scene = new Scene(sp3);
                            primaryStage.setScene(scene);
                            primaryStage.show();
                        }
                    });
                    ap1.getChildren().add(h);
                }
                for (Result i : result) {
                    i.save(new File("data/results/" + file.getName() + " - " + i.getKeyword() + ".txt"));
                }
            } catch (IOException | ParserConfigurationException | SAXException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
