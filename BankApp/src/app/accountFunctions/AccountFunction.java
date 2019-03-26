package app.accountFunctions;

import app.Entities.Account;
import app.db.DB;
import app.login.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class AccountFunction {

    static double amount;


    public static void displayConfirmBox(String message){
        Stage window = getStage();

        Label label = new Label(message);
        Button okBtn = new Button("Ok");

        okBtn.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, okBtn);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static double displayAmountBox(String message){
        Stage window = getStage();

        Label label = new Label(message);
        TextField input = new TextField();

        Button okBtn = new Button("Ok");

        okBtn.setOnAction(event -> {
            try {
                amount = Double.parseDouble(input.getText());
            }catch (Exception e) {
                e.printStackTrace();
            }
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, input, okBtn);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return amount;
    }

    public static void displayAddAccountBox(){
        Stage window = getStage();

        Label nameLbl = new Label("Fyll i egenvalt namn på kontot (valfritt)");
        TextField nameInput = new TextField();

        Label functionLbl = new Label("Välj funktion (valfritt)");
        ComboBox<String> functions= getFunctionsComboBox();


        Button okBtn = new Button("Spara");

        okBtn.setOnAction(event -> {
            String name;
            if(nameInput.getText().isEmpty()){
                name= null;
            }else {
                name=nameInput.getText();
            }

            DB.addAccount(
                    name,
                    functions.getValue()
            );
            window.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(nameLbl, nameInput, functionLbl, functions, okBtn);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayDeleteAccountBox(){
        Stage window = getStage();


        Label label = new Label("Välj konto");
        ComboBox<String> accounts= getAccountComboBox();

        Label errorMessage = new Label("Du måste välja konto");
        errorMessage.setVisible(false);

        Button okBtn = new Button("Spara");

        okBtn.setOnAction(event -> {
            if(getAccountInput(accounts) == null ){
                errorMessage.setVisible(true);
            }else {
                Account account= getAccountInput(accounts);
                if(account.getBalance() > 0 ){
                    displayConfirmBox("Det finns fortfarande pengar på kontot \n vänligen överför pengarna för att kunna ta bort konton");
                    window.close();
                }else {
                    DB.deleteAccount(account.getBankNr());
                    window.close();
                }
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label, accounts, errorMessage, okBtn);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayRenameAccountBox(){
        Stage window = getStage();

        Label accountLbl = new Label("Välj konto");
        ComboBox<String> accounts= getAccountComboBox();

        Label nameLbl = new Label("Fyll i namn på kontot");
        TextField nameInput = new TextField();

        Label errorMessage = new Label("Du måste välja konto och namn");
        errorMessage.setVisible(false);

        Button okBtn = new Button("Spara");

        okBtn.setOnAction(event -> {
            if(nameInput.getText().isEmpty() || getAccountInput(accounts) == null ){
                errorMessage.setVisible(true);
            }else {
                Account account= getAccountInput(accounts);
                DB.upDateAccount(account, "name", nameInput.getText());
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(accountLbl, accounts, nameLbl, nameInput, errorMessage, okBtn);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayAccountFunctions(){
        Stage window = getStage();

        Label accountLbl = new Label("Välj konto");
        ComboBox<String> accounts= getAccountComboBox();
        Label functionLbl = new Label("Välj funktion på ditt konto");
        ComboBox<String> functions= getFunctionsComboBox();
        Button submitBtn = new Button("Spara");

        Label error = new Label();
        error.setVisible(false);

        submitBtn.setOnAction(e -> {
            Account acccount = getAccountInput(accounts);
            if (acccount == null || functions.getValue() == null){
                error.setText("Du måste välja konto och funktion");
                error.setVisible(true);
            }else {
                DB.upDateAccount(acccount,"function",  functions.getValue());
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(accountLbl, accounts, functionLbl, functions, submitBtn, error);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }


    private static Stage getStage(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Bank app");
        window.setMinWidth(250);
        window.setMinHeight(250);
        return window;
    }

    private static ComboBox<String> getAccountComboBox(){
        ComboBox<String> comboBox= new ComboBox<>();
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
        return comboBox;
    }

    private static ComboBox<String> getFunctionsComboBox(){
        ComboBox<String> comboBox= new ComboBox<>();
        comboBox.setPromptText("Välj funktion");
        comboBox.getItems().add("Lönekonto");
        comboBox.getItems().add("Sparkonto");
        comboBox.getItems().add("Kortkonto");

        return comboBox;
    }


    private static Account getAccountInput(ComboBox<String> comboBox){
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


}
