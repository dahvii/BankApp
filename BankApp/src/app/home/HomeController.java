package app.home;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.account.AccountController;
import app.accountFunctions.AccountFunctionWindow;
import app.db.DB;
import app.login.LoginController;
import app.transaction.TransactionController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class HomeController {

    @FXML
    Label userLabel;
    @FXML
    Label  bankMessage;
    @FXML
    VBox accountBox, transactionBox;

    static String messageFromBank="Inga meddelanden";

    @FXML
    void initialize(){
        bankMessage.setText(messageFromBank);
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
        accountBtn.setId(account.getBankNr());
        accountBtn.setOnAction(e -> goToAccount(e));
        if(account.getName() == null){
            accountBtn.setText(account.getBankNr());
        }else {
            accountBtn.setText(account.getName()+"\n"+account.getBankNr());
        }

        Label balance = new Label();
        balance.setText("saldo: "+account.getBalance());


        accountContent.getChildren().addAll(accountBtn, balance);
        accountBox.getChildren().add(accountContent);

    }

    void displayTransactionsSummary(String socialNo){
        List<Transaction> transactions = DB.getUserTransactions(socialNo, 0, 5);

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

    public static void addBankMessage(String message){
        messageFromBank=message;
    }

    @FXML
    void chooseFunction(){
        AccountFunctionWindow.displayAccountFunctions();

    }

    @FXML
    void cardPayment(){
        Account account = DB.accountHasFunction("Kortkonto");
        if(account != null){
            double amount = AccountFunctionWindow.displayAmountBox();
            if (amount > 0) {
                DB.makeCardTransaction(account, amount);
            }
        }else{
            AccountFunctionWindow.displayConfirmBox("Du måste registrera ett kortkonto innan du försöker göra ett kortköp!");
        }

    }

    @FXML
    void planSalary(){
        Account account = DB.accountHasFunction("Lönekonto");
        if(account != null){
            DB.makeSalaryTransaction(account);
        }else{
            AccountFunctionWindow.displayConfirmBox("Du måste registrera ett lönekonto innan du försöker få en lönetransaktion!");
        }
    }

    @FXML
    void goToAccount(ActionEvent event)  {

        Button clicked = (Button)event.getSource();
        Account account = DB.getAccount(clicked.getId());

        FXMLLoader loader = new FXMLLoader( getClass().getResource( "/app/account/account.fxml" ) );
        Parent fxmlInstance = null;
        try {
            fxmlInstance = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene( fxmlInstance, 800, 800 );


        AccountController controller = loader.getController();
        controller.setAccount(account);
        Main.stage.setScene(scene);
        Main.stage.show();

    }
    @FXML
    void goToTransfer(){
        switchScene("/app/transfer/transfer.fxml");
    }
}
