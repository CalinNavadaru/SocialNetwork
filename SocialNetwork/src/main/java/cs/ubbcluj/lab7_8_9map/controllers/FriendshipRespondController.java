package cs.ubbcluj.lab7_8_9map.controllers;

import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.service.ServiceRequests;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.time.LocalDateTime;

public class FriendshipRespondController {
    public Button btnConfirmation;
    public Button btnCancel;
    public RadioButton btnRadioAccept;
    public RadioButton btnRadioRefuse;

    public ToggleGroup toggleGroup;

    ServiceRequests serviceRequests;

    FriendshipStatus status;
    Stage stage;

    @FXML
    public void initialize() {
        toggleGroup = new ToggleGroup();
        btnRadioAccept.setToggleGroup(toggleGroup);
        btnRadioRefuse.setToggleGroup(toggleGroup);
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void handleConfirmation(ActionEvent actionEvent) {
        Long id1 = null, id2 = null;
        if (!toggleGroup.getSelectedToggle().isSelected()) {
            MessageAlert.showErrorMessage(null, "Nu s-a selectat o optiune!");
            stage.close();
        }
        try {
            var r = (RadioButton) toggleGroup.getSelectedToggle();
            if (r == btnRadioAccept)
                status = FriendshipStatus.ACCEPTED;
            else
                status = FriendshipStatus.REJECTED;
        } catch (IllegalArgumentException e) {
            MessageAlert.showErrorMessage(null, "Date invalide!");
            stage.close();
        }
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
