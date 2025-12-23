package application;

import dao.InitDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        InitDatabase.init();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/views/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        stage.setTitle("DompetIn - Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}