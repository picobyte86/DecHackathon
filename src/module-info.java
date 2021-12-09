module DecHackathon {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.media;
    requires java.xml;
    requires java.desktop;
    opens application.Controller;
    opens application.View;
    opens application;
}