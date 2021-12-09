module DecHackathon {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.media;
    opens application.Controller;
    opens application.View;
    opens application;
}