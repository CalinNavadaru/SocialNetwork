package cs.ubbcluj.lab7_8_9map.controllers;

import cs.ubbcluj.lab7_8_9map.domain.dto.DTOMessage;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.events.MessageTaskEvent;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.service.ServiceMessage;
import cs.ubbcluj.lab7_8_9map.service.ServiceUtilizator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

public class MessageController implements Observer<MessageTaskEvent> {

    public TextArea textArea;
    Stage stage;
    public Button btnReplyAll;
    public Button btnSend;
    public TableView<DTOUtilizator> tableViewFriends;
    public TableView<DTOMessage> tableViewMessage;
    public TableColumn<DTOUtilizator, String> tableColumnViewFriends;
    public TableColumn<DTOMessage, String> tableViewColumnMessage;
    ServiceUtilizator serviceUtilizator;
    ServiceMessage serviceMessage;

    Long idActiveuser;

    ObservableList<DTOMessage> modelMessage = FXCollections.observableArrayList();

    ObservableList<DTOUtilizator> modelFriends = FXCollections.observableArrayList();

    public void setService(ServiceUtilizator serviceUtilizator, ServiceMessage serviceMessage, Long id, Stage stage1) {
        this.serviceUtilizator = serviceUtilizator;
        this.serviceMessage = serviceMessage;
        this.serviceMessage.addObserver(this);
        this.stage = stage1;
        idActiveuser = id;
        initModelFriends(id);
        initModelMessage(id);
    }

    private void initModelMessage(Long id) {
        var result = this.serviceMessage.getAllMessagesId(id);
        var currentUser = this.tableViewFriends.getSelectionModel().getSelectedItem();
        if (currentUser == null)
            return;
        var idCurrentUser = currentUser.getId();
        List<DTOMessage> messageList = StreamSupport.stream(result.spliterator(), false).filter(x ->
                (Objects.equals(x.getId1(), idCurrentUser) && Objects.equals(x.getId2(), idActiveuser))
                || (Objects.equals(x.getId1(), idActiveuser) && Objects.equals(x.getId2(), idCurrentUser)))
                .toList();
        modelMessage.setAll(messageList);
        //tableViewMessage.refresh();
        //tableViewMessage.scrollTo(0);
    }

    private void initModelFriends(Long id) {
        var result = this.serviceUtilizator.getAllFriends(id);
        List<DTOUtilizator> friendsList = StreamSupport.stream(result.spliterator(), false).toList();
        modelFriends.setAll(friendsList);
        tableViewFriends.refresh();
    }

    @FXML
    private void initialize() {

        tableColumnViewFriends.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableViewColumnMessage.setCellValueFactory(new PropertyValueFactory<>("Message"));

        tableViewMessage.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewMessage.getSelectionModel().setCellSelectionEnabled(false);
        tableViewFriends.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewFriends.getSelectionModel().setCellSelectionEnabled(false);

        tableViewFriends.setItems(modelFriends);
        tableViewMessage.setItems(modelMessage);

        tableViewFriends.getSelectionModel().selectedIndexProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                var cv = tableViewFriends.getSelectionModel().getSelectedItem().getId();
                initModelMessage(cv);
            }
        }));

        tableViewColumnMessage.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (getTableRow() != null && getTableRow().getItem() != null) {
                        DTOMessage rowData = getTableRow().getItem();
                        if (Objects.equals(rowData.getId1(), idActiveuser)) {
                            setStyle("-fx-alignment: center-left;");
                        } else {
                            setStyle("-fx-alignment: center-right;");
                        }
                    }
                }
            }
        });
    }

    public void handleClose(WindowEvent windowEvent) {
        this.serviceMessage.removeObserver(this);
    }

    @Override
    public void update(MessageTaskEvent messageEvent) {
        initModelMessage(idActiveuser);
    }

    public void handleSend(ActionEvent actionEvent) {
        var text = textArea.getText().trim();
        var user = tableViewFriends.getSelectionModel().getSelectedItem();

        this.serviceMessage.addMessage(new DTOMessage(idActiveuser, user.getId(), LocalDateTime.now(), text, null));
    }


    public void handleReplyAll(ActionEvent actionEvent) {
        var text = textArea.getText().trim();
        tableViewFriends.getItems().stream().forEach(x ->
                this.serviceMessage.addMessage(new DTOMessage(idActiveuser, x.getId(), LocalDateTime.now(), text, null)));
    }
}
