package app.login;


import app.AddToTable;
import app.Entities.Account;
import app.Entities.User;
import app.Main;
import app.db.DB;
import app.db.Database;
import app.db.ObjectMapper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginController {

    @FXML
    TextField socialNoInput ;
    @FXML
    Label errorWrongInput, errorNoInput;
    @FXML
    PasswordField passwordInput;

    // Use this in other Controllers to get "the currently logged in user".
    private static User user = null;
    public static User getUser() { return user; }

    @FXML
    void initialize(){

        // load accounts from db using LoginController.user.getId() and display them

    }



    void switchScene(String pathname) {
        try {
            Parent bla = FXMLLoader.load(getClass().getResource(pathname));
            Scene scene = new Scene(bla, 800, 600);
            Main.stage.setScene(scene);
            Main.stage.show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @FXML
    void login(){
        errorNoInput.setStyle("visibility: hidden;");
        errorWrongInput.setStyle("visibility: hidden;");

        if( !socialNoInput.getText().equals("") && !passwordInput.getText().equals("") ){
            String socialNo = socialNoInput.getText();
            String password = passwordInput.getText();

            user = DB.getMatchingUser(socialNo, password);

            if (user == null){
                errorWrongInput.setStyle("visibility: visible;");
            }
            else{
                goToHome();
            }
            //socialNoInput.clear();
            //passwordInput.clear();
        }
        else{
            errorNoInput.setStyle("visibility: visible;");
        }

    }

    @FXML void goToHome() { switchScene("/app/home/home.fxml"); }
}
