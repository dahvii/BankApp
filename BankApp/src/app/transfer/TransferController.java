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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TransferController {

    @FXML
    Label userLbl, messageLbl;
    @FXML
    HBox toAccountBox, repeatBox;
    @FXML
    ComboBox<String> fromAccount;
    @FXML
    TextField amountInput, messageInput;
    @FXML
    TextField repeatInput= new TextField();
    @FXML
    DatePicker datePicker;
    @FXML
    RadioButton writeInnAccountRbtn, fromOwnAccountRbtn,neverRbtn, monthRbtn, weekRbtn;

    ComboBox<String> toAccountdropdown;
    TextField toAccountTxt = new TextField();

    private double amount;
    private int repeat ;

    @FXML
    private void initialize(){
        userLbl.setText("Inloggad som: "+ LoginController.getUser().getName());
        displayAccountChoice(fromAccount);
        radioBtnFromOwnAccounts();
        messageLbl.setVisible(false);
        datePicker.setValue(LocalDate.now());

    }

    private void displayAccountChoice(ComboBox<String> comboBox){
        List<Account> accounts = DB.getAccounts(LoginController.getUser().getSocialNo());
        comboBox.setPromptText("Välj konto");
        for(Account account: accounts){
            String accountInfo="";
            if (account.getName() != null){
                accountInfo+=account.getName()+"\n";
            }
            if(account.getFunction() != null){
                accountInfo+=account.getFunction()+"\n";
            }
            accountInfo+= account.getBankNr()+"\tsaldo: "+account.getBalance();
            comboBox.getItems().add(accountInfo);

        }
    }

    @FXML
    void submit() throws ParseException {
        Account toAccount = null;

        if (fromOwnAccountRbtn.isSelected()) {
            toAccount = validateToOwnAccount();

        }else {
            toAccount = validateBankNr();
        }

        if (validateInput(toAccount) && toAccount != null){
            buildTransaction(toAccount);
            clearTransactionPage();
        }
    }

    private void buildTransaction(Account toAccount) throws ParseException {
        LocalDate date = datePicker.getValue();
        Date sqlDate = Date.valueOf(date);

        Transaction transaction= new Transaction(
                getAccountInput(fromAccount).getBankNr(),
                toAccount.getBankNr(),
                amount,
                messageInput.getText(),
                sqlDate
        );

        if(monthRbtn.isSelected()){
            DB.planRepeatedTransaction(transaction, true, repeat);
        }else if(weekRbtn.isSelected()){
            DB.planRepeatedTransaction(transaction, false, repeat);
        }

        if(date.isAfter(LocalDate.now())){
            DB.planTransaction(transaction);
        }else {
            DB.makeTransaction(transaction);
        }
    }

    private boolean validateInput(Account toAccount){
        String errorMessage = messageLbl.getText();
        boolean validate=true;

        if(getAccountInput(fromAccount) == null){
            errorMessage+="Välj konto att skicka ifrån\n";
            validate=false;
        }else if(toAccount != null && getAccountInput(fromAccount).getBankNr().equals(toAccount.getBankNr())){
            errorMessage+="Transaktion kan inte göras mellan samma konto, välj annat konto\n";
            validate=false;
        }
        if(datePicker.getValue().isBefore(LocalDate.now())){
            errorMessage+="Välj ett datum som inte har passerat\n";
            validate=false;

        }
        if(monthRbtn.isSelected() ||  weekRbtn.isSelected()){
            if(repeatInput.getText().isEmpty()){
                errorMessage+="Fyll i hur länge du vill upprepa transaktionen\n";
                validate=false;
            }else {
                try {
                    repeat = Integer.parseInt(repeatInput.getText());
                }
                catch (Exception e) {
                    errorMessage+="Fyll i giltigt antal veckor / månader\n";
                    validate=false;
                }

            }
        }
        try {
            amount= Double.parseDouble(amountInput.getText());
            if(amount > getAccountInput(fromAccount).getBalance()){
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
            errorMessage+="Fyll i meddelande\n";
            validate=false;
        }

        if(!validate){
            messageLbl.setText(errorMessage);
            messageLbl.setVisible(true);
        }
        return validate;
    }

    private Account validateToOwnAccount(){
        Account toAccount = getAccountInput(toAccountdropdown);
        if( toAccount == null) {
            messageLbl.setText("Välj konto att skicka till\n");
            messageLbl.setVisible(true);
        }else{
            messageLbl.setText("");
        }
        return toAccount;
    }


    private Account validateBankNr(){
        Matcher matcher = Pattern.compile("^\\d{10}$").matcher(toAccountTxt.getText());
        String errors="";

        if(matcher.matches()){
            Account toAccount = DB.getAccount(toAccountTxt.getText());
            if(toAccount == null){
                errors += "Kontot finns inte\n";
            }else {
                messageLbl.setText("");
                return toAccount;
            }
        }else {
            errors += "Du har inte fyllt i kontonummret rätt\n";
        }
        messageLbl.setText(errors);
        messageLbl.setVisible(true);
        return null;
    }

    private void clearTransactionPage(){
        messageLbl.setText("Transaktion sparad");
        messageLbl.setVisible(true);
        datePicker.setValue(LocalDate.now());
        amountInput.clear();
        messageInput.clear();
        toAccountTxt.clear();
        repeatBox.getChildren().clear();
        neverRbtn.setSelected(true);
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

    @FXML void radioBtnNeverRepeat() {
        repeatBox.getChildren().clear();
    }
    @FXML void radioBtnWeekRepeat() {
        repeatBox.getChildren().clear();
        Label label = new Label("Fyll i hur många veckor");
        repeatBox.getChildren().addAll(label, repeatInput);
    }
    @FXML void radioBtnMonthRepeat() {
        repeatBox.getChildren().clear();
        Label label = new Label("Fyll i hur många månader");
        repeatBox.getChildren().addAll(label, repeatInput);
    }

    @FXML void radioBtnFromOwnAccounts() {
        toAccountBox.getChildren().clear();
        toAccountdropdown = new ComboBox<>();
        displayAccountChoice(toAccountdropdown);
        toAccountBox.getChildren().add(toAccountdropdown);

    }

    @FXML void radioBtnWriteInAccount() {
        toAccountBox.getChildren().clear();

        Label accountLbl = new Label("Fyll i kontonummret du vill skicka till (10 siffror)");

        toAccountBox.getChildren().addAll(accountLbl, toAccountTxt);
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
