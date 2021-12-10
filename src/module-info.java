module DecHackathon {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.media;
    requires java.xml;
    requires java.desktop;
    requires java.net.http;
    requires org.jsoup;
    requires pdfbox.app;
    opens application.Controller;
    opens application.View;
    opens application.Model;
    opens application;
}