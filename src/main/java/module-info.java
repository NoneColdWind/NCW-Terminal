module cn.ncbh.ncw.ncwjavafx {
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

    opens cn.ncbh.ncw.ncwjavafx to javafx.fxml;

    exports cn.ncbh.ncw.ncwjavafx;

    exports cn.ncbh.ncw.ncwjavafx.base;

    exports cn.ncbh.ncw.ncwjavafx.log;
}