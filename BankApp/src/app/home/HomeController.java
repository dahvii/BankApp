package app.home;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.account.AccountController;
import app.account.AccountSummaryController;
import app.db.DB;
import app.db.Database;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jdk.jfr.DataAmount;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class HomeController {

    @FXML
    Label userLabel;
    @FXML
    VBox accountBox, transactionBox;

    @FXML
    void initialize(){
        userLabel.setText("Inloggad som: "+LoginController.getUser().getName());
        displayAccounts();
        displayTransactionsSummary(LoginController.getUser().getSocialNo());
        //displayMyOwnTransactions(LoginController.getUser().getSocialNo());
    }

    void displayAccounts(){
        List<Account> accounts= DB.getAccounts(LoginController.getUser().getSocialNo());
        for(Account account: accounts){
            displayAccount(account);
        }

    }

    void displayAccount(Account account){
        // For every account, do the following:
        HBox accountContent = new HBox();
        Button accountBtn = new Button();
        Label balance = new Label();


        balance.setText("saldo: "+account.getBalance());
        if(account.getName() == null){
            accountBtn.setText(account.getBankNr());
        }else {
            accountBtn.setText(account.getName());
        }

        accountContent.getChildren().addAll(accountBtn, balance);
        accountBox.getChildren().add(accountContent);

    }

    void displayTransactionsSummary(String socialNo){
        List<Transaction> transactions = DB.getUserTransactions(socialNo, 0, 5);

        for (Transaction transaction: transactions){
            displayTransaction(transaction);
        }
    }

    //for testing
    void displayMyOwnTransactions(String socialNo){
        List<Transaction> transactions = DB.getTransactionsBetweenOwnAccounts(socialNo);

        for (Transaction transaction: transactions){
            displayTransaction(transaction);
        }
    }

    void displayTransaction(Transaction transaction){
        // For every transaction, do the following:
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/transaction/transaction.fxml" ) );
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene( fxmlInstance );

            TransactionController controller = loader.getController();

            controller.setTransaction(transaction, DB.getAccounts(LoginController.getUser().getSocialNo()) );

            transactionBox.getChildren().add(scene.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    @FXML
    void goToTransfer() throws IOException {

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/transfer/transfer.fxml" ) );
        Parent fxmlInstance = loader.load();
        Scene scene = new Scene( fxmlInstance, 800, 600 );
        Main.stage.setScene(scene);
        Main.stage.show();

    }
}
