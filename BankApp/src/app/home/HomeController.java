package app.home;

import app.Main;
import app.account.AccountController;
import app.db.DB;
import app.db.Database;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import jdk.jfr.DataAmount;

import java.io.IOException;

public class HomeController {

    @FXML
    Label userLabel;

    @FXML
    void initialize(){
        userLabel.setText("Inloggad som: "+LoginController.getUser().getName());
        System.out.println(LoginController.getUser().getName());



        // load accounts from db using LoginController.user.getId() and display them

    }

    @FXML
    void goToAccount() throws IOException {

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/account/account.fxml" ) );
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene( fxmlInstance, 800, 600 );

        // Make sure that you display "the correct account" based on which one you clicked on
//            AccountController controller = loader.getController();
//            controller.setAccount(accountFromDB);

        // If you don't want to have/use the static variable Main.stage
//        Stage window = (Stage)((Node)e.getSource()).getScene().getWindow();
        Main.stage.setScene(scene);
        Main.stage.show();

    }
}
