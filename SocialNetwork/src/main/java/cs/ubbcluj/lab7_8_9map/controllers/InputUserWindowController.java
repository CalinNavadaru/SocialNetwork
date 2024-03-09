package cs.ubbcluj.lab7_8_9map.controllers;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.service.ServiceUtilizator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class InputUserWindowController {

    ServiceUtilizator serviceUtilizator;

    DTOUtilizator dtoUtilizator;

    Stage stage;

    @FXML
    public TextField textFieldForname;
    @FXML
    public TextField textFieldName;
    @FXML
    public Button btnConfirmation;
    @FXML
    public Button btnCancel;
    @FXML
    public TextField passwordTextField;

    public void setService(ServiceUtilizator s, Stage stage) {
        serviceUtilizator = s;
        this.stage = stage;
    }

    @FXML
    public void initialize() {
    }

    public void handleCancel(){
        stage.close();
    }

    public DTOUtilizator getDtoUtilizator() {
        return dtoUtilizator;
    }

    public void handleConfirmation() {
//        Long id = null;
        String name = null, Forename = null, password = null;
        try {
//            id = Long.parseLong(textFieldID.getText().trim());
            name = textFieldName.getText().trim();
            Forename = textFieldForname.getText().trim();
            password = passwordTextField.getText().trim();
            if (name.isEmpty())
                throw new IllegalArgumentException("Numele utilizatorului invalid");
            if (Forename.isEmpty())
                throw new IllegalArgumentException("Prenumele utilizatorului invalid");
        } catch (IllegalArgumentException e) {
            MessageAlert.showErrorMessage(null,"Date invalide!");
            stage.close();
        }


        dtoUtilizator = new DTOUtilizator(name, Forename, password, 1L);
        stage.close();
    }
}
