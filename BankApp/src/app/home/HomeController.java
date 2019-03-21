package app.home;

import app.Entities.Account;
import app.Entities.Transaction;
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
import javafx.scene.layout.VBox;
import jdk.jfr.DataAmount;

import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    Label userLabel;
    @FXML
    VBox leftContent;

    @FXML
    void initialize(){
        userLabel.setText("Inloggad som: "+LoginController.getUser().getName());

        // load accounts from db using LoginController.user.getId() and display them
        printAccounts();


    }

    void printAccounts(){
        //NEDAN EJ TESTAD PGA KAN INTE FÅ EN LISTA PÅ ACCOUNTS

        //ersättes med List<Account> accounts= DB.getAccounts(LoginController.getUser().getSocialNo());
        Account [] accounts = {new Account(), new Account(), new Account()};

        for(Account account: accounts){
            Label accountName = new Label();
            if(account.getName() == null){
                accountName.setText(account.getBankNr());
            }else {
                accountName.setText(account.getName());
            }
            accountName.setUnderline(true);
            leftContent.getChildren().addAll(accountName);


            //ersättes med List<Transaction> transactions= DB.getTransactions(LoginController.getUser().getSocialNo());
            Transaction [] transactions = {new Transaction(), new Transaction(), new Transaction()};
            //printTransactionsSummary(transactions);


        }
    }

    void printTransactionsSummary(List<Transaction> transactions){
        //NEDAN EJ TESTAD
        for(int i = 0; i < 5; i++){
            displayTransaction(transactions.get(i));
        }

    }

    //städa upp nedan så den är gjord för att visa en transaction
    void displayTransaction(Transaction transaction/*List<Transaction> transactions*/){
        // For every transaction, do the following:
        try {
            FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/transaction/transaction.fxml" ) );
            Parent fxmlInstance = loader.load();
            Scene scene = new Scene( fxmlInstance );

//            TransactionController controller = loader.getController();
//            controller.setTransaction(transaction);

            leftContent.getChildren().add(scene.getRoot());
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
}
