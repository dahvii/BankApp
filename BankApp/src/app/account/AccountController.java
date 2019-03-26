package app.account;


import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.db.DB;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountController {

    @FXML
    VBox transactionBox;
    @FXML
    Label accountLbl, message, userLbl;


    Account account = null;
    int offset;

    public void setAccount(Account account){
        userLbl.setText("Inloggad som: "+LoginController.getUser().getName());
        message.setVisible(false);
        offset = 0;
        this.account= account;
        if(account.getName() != null) {
            accountLbl.setText(account.getName()+"\n"+account.getBankNr());
        }else {
            accountLbl.setText(account.getBankNr());
        }
        loadMoreTransactions();

    }

    @FXML
    void loadMoreTransactions(){
       List<Transaction> transactions = DB.getAccountTransactions(account.getBankNr(), offset, 10);
       offset += 10;

       if(transactions.size() == 0){
           message.setVisible(true);
       }

       for(Transaction transaction: transactions){
           displayTransaction(transaction);
       }
    }

    void displayTransaction(Transaction transaction){
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

    @FXML void goToHome() { switchScene("/app/home/home.fxml"); }
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
}
