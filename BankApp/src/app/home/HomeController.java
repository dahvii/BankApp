package app.home;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.account.AccountController;
import app.accountFunctions.AccountFunction;
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
    Label userLbl;
    @FXML
    VBox accountBox, transactionBox;

    @FXML
    void initialize(){
        userLbl.setText("Inloggad som: "+LoginController.getUser().getName());
        displayAccounts();
        displayTransactionsSummary(LoginController.getUser().getSocialNo());
    }

    void displayAccounts(){
        List<Account> accounts= DB.getAccounts(LoginController.getUser().getSocialNo());
        for(Account account: accounts){
            displayAccount(account);
        }

    }

    void displayAccount(Account account){
        HBox accountContent = new HBox();

        Button accountBtn = new Button();
        accountBtn.setId(account.getBankNr());
        accountBtn.setOnAction(e -> goToAccount(e));

        String accountInfo="";
        if (account.getName() != null){
            accountInfo+=account.getName()+"\n";
        }
        if(account.getFunction() != null){
            accountInfo+=account.getFunction()+"\n";
        }
        accountInfo+= account.getBankNr();
        accountBtn.setText(accountInfo);

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


    @FXML
    void addAccount(){
        AccountFunction.displayAddAccountBox();
        reload();
    }

    @FXML
    void changeLimit(){
        Account account = DB.accountHasFunction("Kortkonto");
        if(account != null){
            int limit = (int) AccountFunction.displayAmountBox("Fyll i summan för saldotak");
            if (limit > 0){
                DB.upDateBoundary(account, limit);
            }
        }else{
            AccountFunction.displayConfirmBox("Du måste registrera ett kortkonto innan du kan ändra saldotak!");
        }
    }

    @FXML
    void renameAccount(){
        AccountFunction.displayRenameAccountBox();
        reload();
    }

    @FXML
    void deleteAccount(){
        AccountFunction.displayDeleteAccountBox();
        reload();
    }
    @FXML
    void chooseFunction(){
        AccountFunction.displayAccountFunctions();
    }

    @FXML
    void cardPayment(){
        Account account = DB.accountHasFunction("Kortkonto");
        if(account != null){
            double amount = AccountFunction.displayAmountBox("Fyll i summan för kortköpet");
            if (amount > 0) {
                DB.makeCardTransaction(account, amount);
            }
        }else{
            AccountFunction.displayConfirmBox("Du måste registrera ett kortkonto innan du försöker göra ett kortköp!");
        }
        reload();
    }

    @FXML
    void planSalary(){
        Account account = DB.accountHasFunction("Lönekonto");
        if(account != null){
            DB.makeSalaryTransaction(account);
        }else{
            AccountFunction.displayConfirmBox("Du måste registrera ett lönekonto innan du försöker få en lönetransaktion!");
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

    void reload(){
        accountBox.getChildren().clear();
        transactionBox.getChildren().clear();
        displayAccounts();
        displayTransactionsSummary(LoginController.getUser().getSocialNo());
    }
}
