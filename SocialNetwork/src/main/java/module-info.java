module cs.ubbcluj.lab7_8_9map {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires java.sql;


    exports cs.ubbcluj.lab7_8_9map;
    exports cs.ubbcluj.lab7_8_9map.controllers;
    exports cs.ubbcluj.lab7_8_9map.domain.dto;
    exports cs.ubbcluj.lab7_8_9map.domain.validators;
    exports cs.ubbcluj.lab7_8_9map.domain;
    exports cs.ubbcluj.lab7_8_9map.events;
    exports cs.ubbcluj.lab7_8_9map.exceptions;
    exports cs.ubbcluj.lab7_8_9map.service;
    exports cs.ubbcluj.lab7_8_9map.observer;
    exports cs.ubbcluj.lab7_8_9map.repository;
    opens cs.ubbcluj.lab7_8_9map to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.controllers to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.domain.dto to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.domain to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.domain.validators to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.repository to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.events to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.observer to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.service to javafx.fxml;
    opens cs.ubbcluj.lab7_8_9map.exceptions to javafx.fxml;
}