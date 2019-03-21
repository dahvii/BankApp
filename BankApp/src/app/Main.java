package app;

import app.Entities.Transaction;
import app.transaction.Transact;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Date;
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

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date= format.parse ( "2009-12-31" );
        java.sql.Date sqlDate=new java.sql.Date(date.getTime());

        Transaction transaction = new Transaction("0063527510", "0024480606", 4000, "hyra", null, sqlDate);

        //Transact.makeTransaction(transaction);

        System.out.println(Transact.subtractMoney("0022717455", 5000));

        launch(args);
    }
}
