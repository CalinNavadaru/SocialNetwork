package cs.ubbcluj.lab7_8_9map.controllers;

import cs.ubbcluj.lab7_8_9map.Main;
import cs.ubbcluj.lab7_8_9map.domain.FriendshipStatus;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOUtilizator;
import cs.ubbcluj.lab7_8_9map.events.CerereTaskEvent;
import cs.ubbcluj.lab7_8_9map.events.Event;
import cs.ubbcluj.lab7_8_9map.events.PrietenieTaskEvent;
import cs.ubbcluj.lab7_8_9map.events.UtilizatorTaskEvent;
import cs.ubbcluj.lab7_8_9map.exceptions.LoginException;
import cs.ubbcluj.lab7_8_9map.exceptions.ValidationException;
import cs.ubbcluj.lab7_8_9map.observer.Observer;
import cs.ubbcluj.lab7_8_9map.repository.Page;
import cs.ubbcluj.lab7_8_9map.repository.Pageable;
import cs.ubbcluj.lab7_8_9map.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainWindowController implements Observer<Event> {


    public TextField textFieldPage;
    public Button btnJumpPage;
    public Button btnPrvPage;
    public Button btnNextPage;
    public TextField textFieldPage2;
    public Button btnNumberPages;
    public TextField textFieldFriendshipPage;
    public Button btnFriendshipJump;
    public Button btnPrevFriendships;
    public Button btnNextFriendships;
    public TextField textFieldPageNumber2;
    public Button btnApplyF;
    private ServiceUtilizator serviceUtilizator;
    private ServicePrietenie servicePrietenie;
    private ServiceRequests serviceRequests;
    private ServiceStatistici serviceStatistici;
    private ServiceMessage serviceMessage;
    private ServiceLogin serviceLogin;
    private Integer pageSize = null;
    private Integer pageNumber = null;

    private Integer totalElementCount;

    private Integer pageSizeF = null;
    private Integer pageNumberF = null;

    private Integer totalElementCountF;
    ObservableList<DTOUtilizator> modelUtilizator = FXCollections.observableArrayList();

    ObservableList<DTOPrietenie> modelPrietenie = FXCollections.observableArrayList();

    ObservableList<DTOCererePrietenie> modelCereri = FXCollections.observableArrayList();

    public Button btnMessage;
    public TextField textFieldMessage;
    @FXML
    public Button btnAddUser, btnDeleteUser, btnModifyUser;
    public Button btnAddFriendships, btnDeleteFriendship, btnNrComunities, btnMostSociableComunity, btnRespondFriendship;
    @FXML
    public TableView<DTOUtilizator> tableUsers;
    @FXML
    public TableColumn<DTOUtilizator, Long> tableColumnID;
    @FXML
    public TableColumn<DTOUtilizator, String> tableColumnName;
    @FXML
    public TableColumn<DTOUtilizator, String> tableColumnLastName;

    @FXML
    public TableView<DTOPrietenie> tableViewFriendships;
    @FXML
    public TableColumn<DTOPrietenie, String> tableColumnUser1;
    @FXML
    public TableColumn<DTOPrietenie, String> tableColumnUser2;
    @FXML
    public TableColumn<DTOPrietenie, LocalDateTime> tableColumnFriendshipDate;
    @FXML
    public TableColumn<DTOPrietenie, FriendshipStatus> tableColumnFriendshipStatus;

    @FXML
    public TableView<DTOCererePrietenie> tableViewPendingFriendships;
    @FXML
    public TableColumn<DTOCererePrietenie, String> tablePendingColumnUser1;
    @FXML
    public TableColumn<DTOCererePrietenie, String> tablePendingColumnUser2;
    @FXML
    public PasswordField passwordField;


    public void setService(ServiceUtilizator s, ServicePrietenie p,
                           ServiceRequests serviceRequests, ServiceStatistici serviceS,
                           ServiceMessage serviceMessage, ServiceLogin serviceLogin) {
        serviceUtilizator = s;
        this.serviceLogin = serviceLogin;
        this.serviceMessage = serviceMessage;
        servicePrietenie = p;
        this.serviceStatistici = serviceS;
        this.serviceRequests = serviceRequests;
        serviceUtilizator.addObserver(this);
        serviceRequests.addObserver(this);
        servicePrietenie.addObserver(this);
        initModelUsers2();
        initModelRequests();
        initModelPrietenie2();
    }

    @FXML
    public void initialize() {
        tableColumnID.setCellValueFactory(new PropertyValueFactory<>("Id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("FirstName"));
        tableColumnLastName.setCellValueFactory(new PropertyValueFactory<>("LastName"));
        tableUsers.getSelectionModel().setCellSelectionEnabled(false);
        tableUsers.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableUsers.setItems(modelUtilizator);

        tablePendingColumnUser1.setCellValueFactory(new PropertyValueFactory<>("Id1"));
        tablePendingColumnUser2.setCellValueFactory(new PropertyValueFactory<>("Id2"));
        tableViewPendingFriendships.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewPendingFriendships.getSelectionModel().setCellSelectionEnabled(false);
        tableViewPendingFriendships.setItems(modelCereri);

        tableViewFriendships.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableViewFriendships.getSelectionModel().setCellSelectionEnabled(false);
        tableViewFriendships.setItems(modelPrietenie);
        tableColumnUser1.setCellValueFactory(new PropertyValueFactory<>("Id1"));
        tableColumnUser2.setCellValueFactory(new PropertyValueFactory<>("Id2"));
        tableColumnFriendshipDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tableColumnFriendshipStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
    }

    private void initModelUsers() {
        Iterable<DTOUtilizator> users = serviceUtilizator.getAllEntities();
        List<DTOUtilizator> messageTaskList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        modelUtilizator.setAll(messageTaskList);
        tableUsers.refresh();
    }
    private void initModelUsers2() {
        if (pageNumber == null)
            pageNumber = 0;
        if (pageSize == null)
            pageSize = 5;
        Page<DTOUtilizator> users = serviceUtilizator.findAllPaginat(new Pageable(pageNumber, pageSize));
        int maxPage = (int) Math.ceil((double)users.getTotalElementCount()/ pageSize) - 1;
        if (pageNumber > maxPage) {
            pageNumber = maxPage;
            users = serviceUtilizator.findAllPaginat(new Pageable(pageNumber, pageSize));
        }
        else if (pageNumber < 0) {
            pageNumber = 0;
            users = serviceUtilizator.findAllPaginat(new Pageable(pageNumber, pageSize));
        }
        modelUtilizator.setAll(StreamSupport.stream(users.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList()));
        totalElementCount = users.getTotalElementCount();
        btnPrvPage.setDisable(pageNumber == 0);
        btnNextPage.setDisable((pageNumber + 1) * pageSize >= totalElementCount);
        if (pageNumber > 0 && ((pageNumber + 1) * pageSize < totalElementCount)) {
            btnPrvPage.setDisable(false);
            btnNextPage.setDisable(false);
        }
        tableUsers.refresh();
    }


    private void initModelRequests() {
        Iterable<DTOCererePrietenie> users = serviceRequests.getAllEntities();
        List<DTOCererePrietenie> messageTaskList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        modelCereri.setAll(messageTaskList);
        tableViewPendingFriendships.refresh();
    }

    private void initModelPrietenie() {
        Iterable<DTOPrietenie> users = servicePrietenie.getAllEntities();
        List<DTOPrietenie> messageTaskList = StreamSupport.stream(users.spliterator(), false).collect(Collectors.toList());
        modelPrietenie.setAll(messageTaskList);
        tableViewFriendships.refresh();
    }

    private void initModelPrietenie2() {
        if (pageNumberF == null)
            pageNumberF = 0;
        if (pageSizeF == null)
            pageSizeF = 5;
        Page<DTOPrietenie> friendships = servicePrietenie.findAllPaginat(new Pageable(pageNumberF, pageSizeF));
        int maxPageF = (int) Math.ceil((double) friendships.getTotalElementCount() / pageSizeF) - 1;
        if (pageNumberF > maxPageF) {
            pageNumberF = maxPageF;
            friendships = servicePrietenie.findAllPaginat(new Pageable(pageNumberF, pageSizeF));
        } else if (pageNumberF < 0) {
            pageNumberF = 0;
            friendships = servicePrietenie.findAllPaginat(new Pageable(pageNumberF, pageSizeF));
        }
        modelPrietenie.setAll(StreamSupport.stream(friendships.getElementsOnPage().spliterator(), false)
                .collect(Collectors.toList()));
        totalElementCountF = friendships.getTotalElementCount();
        btnPrevFriendships.setDisable(pageNumberF == 0);
        btnNextFriendships.setDisable((pageNumberF + 1) * pageSizeF >= totalElementCountF);
        if (pageNumberF > 0 && ((pageNumberF + 1) * pageSizeF < totalElementCountF)) {
            btnPrevFriendships.setDisable(false);
            btnNextFriendships.setDisable(false);
        }
        tableViewFriendships.refresh();

    }

    public void handleDeleteUser() {
        var cv = tableUsers.getSelectionModel().getSelectedItem();
        if (cv == null) {
            MessageAlert.showErrorMessage(null, "Nu a fost selectat ceva!");
            return;
        }
        var result = this.serviceUtilizator.deleteEntity(cv.getId());
        if (result != null) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere", "Utilizatorul a fost sters!");
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Stergere", "Utilizatorul nu a fost sters!");
        }
    }

    public void handleAddUser() {
        DTOUtilizator utilizator;
        try {
            var controller = initUserInputWindow();

            utilizator = controller.getDtoUtilizator();
            var pass = serviceLogin.encrypt(utilizator.getPassword());
            var r = this.serviceUtilizator.addEntity(new DTOUtilizator(utilizator.getFirstName(), utilizator.getLastName(),
                    pass, utilizator.getId()));
            if (r != null) {
                MessageAlert.showErrorMessage(null, "Utilizatorul exista deja!");
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Reusita!", "Utiliztorul a fost introdus!");
            }
        } catch (IOException e) {
            throw new RuntimeException("A aparut o eroare la afisare ferestrei!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, "Date utilizator invalide(Numele incepe cu litera mare!)!");
        }
    }

    public void handlerModifyUser() {
        var cv = tableUsers.getSelectionModel().getSelectedItem();
        if (cv == null) {
            MessageAlert.showErrorMessage(null, "Nu a fost selectat ceva!");
            return;
        }
        var id = cv.getId();
        try {
            var controller = initUserInputWindow();

            DTOUtilizator utilizator = controller.getDtoUtilizator();
            utilizator.setId(id);
            var r = this.serviceUtilizator.updateEntity(utilizator);
            if (r == null) {
                MessageAlert.showErrorMessage(null, "Utilizatorul exista deja!");
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Reusita!", "Utiliztorul a fost actualizat!");
            }
        } catch (IOException e) {
            throw new RuntimeException("A aparut o eroare la afisare ferestrei!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, "Date utilizator invalide(Numele incepe cu litera mare!)!");
        }
    }

    private InputUserWindowController initUserInputWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("input-user-window.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Utilizator");
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        InputUserWindowController userWindowController = loader.getController();
        userWindowController.setService(serviceUtilizator, stage);

        stage.showAndWait();

        return userWindowController;
    }

    private RequestWindowController initRequestInputWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("input-request-window.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Cerere");
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        RequestWindowController requestWindowController = loader.getController();
        requestWindowController.setService(serviceRequests, stage);

        stage.showAndWait();

        return requestWindowController;
    }

    private FriendshipRespondController initRespondInputWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("input-friendship-respond-window.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Confirmare");
        stage.initModality(Modality.WINDOW_MODAL);

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        FriendshipRespondController friendshipRespondController = loader.getController();
        friendshipRespondController.setService(serviceRequests, stage);

        stage.showAndWait();

        return friendshipRespondController;
    }

    @Override
    public void update(Event event) {
        if (event instanceof UtilizatorTaskEvent) {
            initModelUsers2();
        } else if (event instanceof CerereTaskEvent) {
            initModelRequests();
        } else if (event instanceof PrietenieTaskEvent) {
            initModelPrietenie2();
        }
    }

    public void handleNrComunities() {
        var result = this.serviceStatistici.getNrComunities();
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Numar comunitati",
                "Numarul de comunitati este " + result + "!");
    }

    public void handleMostSociableComunity() {
        var result = this.serviceStatistici.getMostSociableComunity();
        var resultString = "";
        for (var user : result) {
            resultString = resultString.concat("\n" + user.toString());
        }
        MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Cea mai sociabila comunitate",
                "Cea mai sociabila comunitate este " + resultString);
    }

    public void handleAddRequest() {
        DTOCererePrietenie cererePrietenie;
        try {
            var controller = initRequestInputWindow();

            cererePrietenie = controller.getDtoCererePrietenie();
            var r = this.serviceRequests.addEntity(cererePrietenie);
            if (r != null) {
                MessageAlert.showErrorMessage(null, "Cereerea exista deja!");
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.CONFIRMATION, "Reusita!",
                        "Cererea a fost introdusa!");
            }
        } catch (IOException e) {
            throw new RuntimeException("A aparut o eroare la afisare ferestrei!");
        } catch (ValidationException e) {
            MessageAlert.showErrorMessage(null, e.getMessage());
        }
    }

    public void handleDeleteFriendship() {
        var cv = tableViewFriendships.getSelectionModel().getSelectedItem();
        if (cv == null) {
            MessageAlert.showErrorMessage(null, "Nu a fost selectat ceva!");
            return;
        }
        var result = this.servicePrietenie.deleteEntity(cv.getId());
        if (result != null) {
            MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Stergere",
                    "Prietenia a fost stearsa!");
        } else {
            MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Stergere",
                    "Prietenia nu a fost stearsa!");
        }
    }

    public void handleRespondFriendship() {
        var cv = tableViewPendingFriendships.getSelectionModel().getSelectedItem();
        if (cv == null) {
            MessageAlert.showErrorMessage(null, "Nu a fost selectat ceva!");
            return;
        }
        try {
            var controller = initRespondInputWindow();
            var dto = controller.getStatus();
            var dtoPrietenie = new DTOPrietenie(LocalDateTime.now(), dto, cv.getId());
            this.serviceRequests.deleteEntity(cv.getId());
            var r2 = this.servicePrietenie.addEntity(dtoPrietenie);
            if (r2 == null) {
                MessageAlert.showMessage(null, Alert.AlertType.INFORMATION, "Raspuns",
                        "Prietenia a fost confirmata/infirmata cu succes!");
            } else {
                MessageAlert.showMessage(null, Alert.AlertType.ERROR, "Raspuns",
                        "Prietenia nu a fost confirmata/infirmata cu succes!");
            }
        } catch (IOException e) {
            throw new RuntimeException("Erorare!");
        }
    }

    private void initMessageWindow(Long id) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(Main.class.getResource("message-window.fxml"));

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setTitle("Mesaje");
        stage.initModality(Modality.WINDOW_MODAL);


        Scene scene = new Scene(loader.load());
        stage.setScene(scene);

        MessageController messageController = loader.getController();
        stage.setOnCloseRequest(messageController::handleClose);
        messageController.setService(serviceUtilizator, serviceMessage, id, stage);

        stage.showAndWait();
    }

    public void handleMessage() throws IOException {
        var cv = this.textFieldMessage.getText().trim();
        var pass = this.passwordField.getText();
        Long id;
        try {
            id = Long.parseLong(cv);
            var user = this.serviceUtilizator.findOneEntity(id);
            this.serviceLogin.tryLogin(user, pass);
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Eroare!\nID INVALID!");
            return;
        } catch (LoginException e) {
            MessageAlert.showErrorMessage(null, "Parola invalida!");
            return;
        }
        this.textFieldMessage.clear();
        initMessageWindow(id);
    }

    public void handlerNextPage(ActionEvent actionEvent) {
        pageNumber++;
        initModelUsers2();
    }

    public void handlerPrevPage(ActionEvent actionEvent) {
        pageNumber--;
        initModelUsers2();
    }

    public void handleJumpPage(ActionEvent actionEvent) {
        var pageN = this.textFieldPage.getText().trim();
        try {
            pageNumber = Integer.parseInt(pageN);
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Ati introdus o valoare invalida!");
            return;
        }
        initModelUsers2();
    }

    public void handleChangeNumberPages(ActionEvent actionEvent) {
        var nr = textFieldPage2.getText().trim();
        try {
            pageSize = Integer.parseInt(nr);
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Ati introdus o valoare invalida!");
            return;
        }
        initModelUsers2();
    }

    public void handlerPrevPage2(ActionEvent actionEvent) {
        pageNumberF--;
        initModelPrietenie2();
    }

    public void handlerNextPage2(ActionEvent actionEvent) {
        pageNumberF++;
        initModelPrietenie2();
    }

    public void handlerJumpFriendships(ActionEvent actionEvent) {
        var nr = textFieldFriendshipPage.getText().trim();
        try {
            pageNumberF = Integer.parseInt(nr) - 1;
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Ati introdus o valoare invalida!");
            return;
        }
        initModelPrietenie2();
    }

    public void handleFriendshipsChangeNumberPages(ActionEvent actionEvent) {
        var nr = textFieldPageNumber2.getText().trim();
        try {
            pageSizeF = Integer.parseInt(nr);
        } catch (NumberFormatException e) {
            MessageAlert.showErrorMessage(null, "Ati introdus o valoare invalida!");
            return;
        }
        initModelPrietenie2();
    }

}
