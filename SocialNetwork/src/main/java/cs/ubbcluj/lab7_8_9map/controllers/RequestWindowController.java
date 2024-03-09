package cs.ubbcluj.lab7_8_9map.controllers;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.service.ServiceRequests;
import cs.ubbcluj.lab7_8_9map.service.ServiceUtilizator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RequestWindowController {
    public Button btnConfirmation;
    public Button btnCancel;
    public TextField textFieldId2;
    public TextField textFieldId1;

    ServiceRequests serviceRequests;

    public DTOCererePrietenie getDtoCererePrietenie() {
        return dtoCererePrietenie;
    }

    DTOCererePrietenie dtoCererePrietenie;
    Stage stage;

    @FXML
    public void initialize() {
    }

    public void handleConfirmation(ActionEvent actionEvent) {
        Long id1 = null, id2 = null;
        try {
            id1 = Long.parseLong(textFieldId1.getText().trim());
            id2 = Long.parseLong(textFieldId2.getText().trim());
        } catch (IllegalArgumentException e) {
            MessageAlert.showErrorMessage(null,"Date invalide!");
            stage.close();
        }

        dtoCererePrietenie = new DTOCererePrietenie(FriendshipStatus.PENDING, new Tuple<>(id1, id2));
        stage.close();
    }

    public void handleCancel(ActionEvent actionEvent) {
        stage.close();
    }

    public void setService(ServiceRequests serviceRequests, Stage stage) {
        this.serviceRequests = serviceRequests;
        this.stage = stage;
    }
}
