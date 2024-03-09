package cs.ubbcluj.lab7_8_9map;

import cs.ubbcluj.lab7_8_9map.controllers.MainWindowController;
import cs.ubbcluj.lab7_8_9map.domain.CererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.Prietenie;
import cs.ubbcluj.lab7_8_9map.domain.Tuple;
import cs.ubbcluj.lab7_8_9map.domain.Utilizator;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOCererePrietenie;
import cs.ubbcluj.lab7_8_9map.domain.dto.DTOPrietenie;
import cs.ubbcluj.lab7_8_9map.repository.*;
import cs.ubbcluj.lab7_8_9map.service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    private final PagingRepository<Long, Utilizator> repository = new DBRepositoryUtilizator("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Grenada#123");
    private final PagingRepository<Tuple<Long, Long>, Prietenie> prietenierepository = new DBRepositoryPrietenie("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Grenada#123");
    private final Repository<Tuple<Long, Long>, CererePrietenie> cererePrietenieRepository = new DBRepositoryCererePrietenie("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Grenada#123");

    private final DBRepositoryMessage repositoryMessage = new DBRepositoryMessage("jdbc:postgresql://localhost:5432/socialnetwork", "postgres", "Grenada#123");
    private final ServiceUtilizator service = new ServiceUtilizator(repository);
    private final Service<Tuple<Long, Long>, DTOPrietenie> prietenieService = new ServicePrietenie(repository, prietenierepository);
    private final Service<Tuple<Long, Long>, DTOCererePrietenie> cererePrietenieService = new ServiceRequests(repository, prietenierepository, cererePrietenieRepository);
    private final ServiceStatistici serviceStatistici = new ServiceStatistici(repository, prietenierepository);

    private final ServiceMessage serviceMessage = new ServiceMessage((DBRepositoryUtilizator) repository, repositoryMessage);

    private final ServiceLogin serviceLogin = new ServiceLogin(repository);

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(Main.class.getResource("main-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        initWindow(fxmlLoader);
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void initWindow(FXMLLoader fxmlLoader) {

        MainWindowController controller = fxmlLoader.getController();
        controller.setService(service, (ServicePrietenie) prietenieService, (ServiceRequests) cererePrietenieService,
                serviceStatistici, serviceMessage, serviceLogin);
    }

    public static void main(String[] args) {
        launch();
    }
}