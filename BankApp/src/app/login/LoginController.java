package app.login;


import app.Entities.User;
import app.Main;
import app.db.DB;
import app.db.Database;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController {

    @FXML
    TextField socialNoInput, passwordInput;
    @FXML
    Label errorWrongInput, errorNoInput;

    // Use this in other Controllers to get "the currently logged in user".
    private static User user = null;
    public static User getUser() { return user; }

    @FXML
    private void initialize() {
        System.out.println("initialize login");
        loadUser();
    }

    void loadUser(){
//        user = DB.getMatchingUser("Kalle", "abc123?");
        // if null display error
        // else switchScene to Home
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
        //Database.getInstance();


        if( !socialNoInput.getText().equals("") && !passwordInput.getText().equals("") ){
            String socialNo = socialNoInput.getText();
            String password = passwordInput.getText();

            errorWrongInput.setStyle("visibility: visible;");
            System.out.println(socialNo+"\t"+password);
            socialNoInput.clear();
            passwordInput.clear();
        }
        else{
            errorNoInput.setStyle("visibility: visible;");
        }

    }

    @FXML void goToHome() { switchScene("/app/home/home.fxml"); }
}
