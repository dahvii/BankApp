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

public class AccountFunctionWindow {

    static double amount;


    private static void display(String title, String message1, String message2){
        Stage window = new Stage();

        //blockar byte av fönster
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setMinWidth(250);

        Label label1 = new Label();
        label1.setText(message1);

        ComboBox<String> accounts= getAccountComboBox();

        Label label2 = new Label();
        label2.setText(message2);

        ComboBox<String> functions= getAccountComboBox();

        Button submitBtn = new Button("Spara");

        //ska vi returnera nått??
        submitBtn.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label1, accounts, label2, functions, submitBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static void displayConfirmBox(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Bank app");
        window.setMinWidth(250);

        Label label = new Label(message);

        Button okBtn = new Button("Ok");

        okBtn.setOnAction(e -> window.close());

        VBox layout = new VBox(10);
        layout.getChildren().addAll(label);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }

    public static double displayAmountBox(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Bank app");
        window.setMinWidth(250);

        Label label = new Label("Fyll i summan för kortköpet");
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
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        return amount;
    }

    public static void displayAccountFunctions(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Välj funktion");
        window.setMinWidth(250);

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
                DB.setAccountFunction(acccount, functions.getValue());
                window.close();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(accountLbl, accounts, functionLbl, functions, submitBtn, error);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
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
