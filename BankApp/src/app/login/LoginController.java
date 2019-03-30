package app.login;
import app.Entities.User;
import app.Main;
import app.db.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML
    TextField socialNoInput ;
    @FXML
    Label errorWrongInput, errorNoInput;
    @FXML
    PasswordField passwordInput;
    @FXML
    Button logInBtn;

    private static User user = null;
    public static User getUser() { return user; }

    @FXML
    void initialize(){
        logInBtn.setDefaultButton(true);
    }



    void switchScene(String pathname) {
        try {
            Parent parent = FXMLLoader.load(getClass().getResource(pathname));
            Scene scene = new Scene(parent, 800, 800);
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
        }
        else{
            errorNoInput.setStyle("visibility: visible;");
        }

    }

    @FXML void goToHome() { switchScene("/app/home/home.fxml"); }
}
