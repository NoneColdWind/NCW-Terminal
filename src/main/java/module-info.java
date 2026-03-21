module cn.ncw.javafx.ncwjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.jfoenix;
    requires jdk.compiler;
    requires java.desktop;
    requires jlayer;
    requires com.almasb.fxgl.entity;
    requires com.sun.jna;
    requires annotations;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires javafx.media;
    requires NCW.Logger;
    requires Music;
    requires NCWUtils;

    opens cn.ncw.javafx.ncwjavafx to javafx.fxml;

    exports cn.ncw.javafx.ncwjavafx;

    exports cn.ncw.javafx.ncwjavafx.base;

}