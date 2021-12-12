module DecHackathon {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.base;
    requires javafx.media;
    requires java.xml;
    requires java.desktop;
    requires java.net.http;
    requires org.jsoup;
    requires jdk.jsobject;
    requires org.json;
    requires pdfbox.app;
    requires java.sql;
    requires jackson.core.asl;
    requires jackson.mapper.asl;
    opens application.Controller;
    opens application.View;
    opens application.Model;
    opens application;
    opens application.Model.Types;
}