package app;

import app.Entities.Transaction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main extends Application {
    public static Stage stage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        // First FXML that should be displayed is the Login
        // after successful login you should get transferred to Home
        Parent root = FXMLLoader.load(getClass().getResource("/app/login/login.fxml"));
        primaryStage.setTitle("Bank app");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) throws ParseException {
        launch(args);
    }
}
