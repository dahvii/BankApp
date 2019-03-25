package app.transfer;


import app.Entities.Account;
import app.Entities.Transaction;
import app.Main;
import app.db.DB;
import app.login.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

public class TransferController {

    @FXML
    Label userLabel, messageLbl;
    @FXML
    HBox toAccountBox;
    @FXML
    ComboBox<String> fromAccount;
    @FXML
    ToggleGroup radioBtnGroup;
    @FXML
    TextField amountInput, messageInput;
    @FXML
    DatePicker datePicker;

    ComboBox<String> toAccount;

    @FXML
    private void initialize(){
        userLabel.setText("Inloggad som: "+ LoginController.getUser().getName());
        displayAccountChoice(fromAccount);
        radioBtnFromOwnAccounts();
        messageLbl.setVisible(false);
        datePicker.setValue(LocalDate.now());

    }

    private void displayAccountChoice(ComboBox<String> comboBox){
        List<Account> accounts = DB.getAccounts(LoginController.getUser().getSocialNo());
        comboBox.setPromptText("Välj konto");
        for(Account account: accounts){
            if (account.getName() != null){
                comboBox.getItems().add(account.getName()+"\tsaldo: "+account.getBalance()+"\n"+account.getBankNr());
            }else {
                comboBox.getItems().add(account.getBankNr()+"\tsaldo: "+account.getBalance());
            }
        }
    }

    @FXML
    void submit() throws ParseException {
        if(validateInput()) {
            buildTransaction();
            clearTransactionPage();
        }
    }

    private void buildTransaction() throws ParseException {
        LocalDate date = datePicker.getValue();
        Date sqlDate = Date.valueOf(date);

        Transaction transaction= new Transaction(
                getAccountInput(fromAccount).getBankNr(),
                getAccountInput(toAccount).getBankNr(),
                Double.parseDouble(amountInput.getText()),
                messageInput.getText(),
                sqlDate
        );

        if(date.isAfter(LocalDate.now())){
            DB.planTransaction(transaction);
        }else {
            DB.makeTransaction(transaction);
        }
    }

    private boolean validateInput(){
        messageLbl.setVisible(false);
        String errorMessage = "";
        double amount=0;
        boolean validate=true;

        if(getAccountInput(fromAccount) == null || getAccountInput(toAccount) == null){
            errorMessage+="Välj konto\n";
            validate=false;
        }else if(getAccountInput(fromAccount).getBankNr().equals(getAccountInput(toAccount).getBankNr())){
            errorMessage+="Transaktion kan inte göras mellan samma konto, välj annat konto\n";
            validate=false;
        }
        if(datePicker.getValue().isBefore(LocalDate.now())){
            errorMessage+="Välj ett datum som inte har passerat\n";
            validate=false;

        }
        try {
            amount= Double.parseDouble(amountInput.getText());
            if(amount >= getAccountInput(fromAccount).getBalance()){
                errorMessage+="För lite pengar på kontot för vald summa\n";
                validate=false;
            }
        }
        catch (Exception e) {
            errorMessage+="Fyll i giltig summa\n";
            validate=false;
        }
        if(messageInput.getText().length() > 50){
            errorMessage+="För lång meddelande-text\n";
            validate=false;
        }
        if(messageInput.getText().isEmpty()){
            errorMessage+="Fyll i meddelande";
            validate=false;
        }

        if(!validate){
            messageLbl.setText(errorMessage);
            messageLbl.setVisible(true);
        }
        return validate;
    }

    private void clearTransactionPage(){
        messageLbl.setText("Transaktion sparad");
        messageLbl.setVisible(true);
        datePicker.setValue(LocalDate.now());
        amountInput.clear();
        messageInput.clear();
    }

    private Account getAccountInput(ComboBox<String> comboBox){
        if(comboBox.getValue() == null){
            return null;
        }
        Account choosenAccount=null;
        List<Account> accounts = DB.getAccounts(LoginController.getUser().getSocialNo());
        for(Account account: accounts){
            if(comboBox.getValue().contains(account.getBankNr())){
                choosenAccount=account;
            }
        }
        return choosenAccount;
    }


    @FXML void radioBtnFromOwnAccounts() {
        toAccountBox.getChildren().clear();
        toAccount = new ComboBox<>();
        displayAccountChoice(toAccount);
        toAccountBox.getChildren().add(toAccount);

    }

    @FXML void radioBtnWriteInAccount() {
        toAccountBox.getChildren().clear();
        System.out.println("du vill skriva in konto själv jao");
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
