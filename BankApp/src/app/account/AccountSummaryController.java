package app.account;


import app.Entities.Account;
import app.Entities.Transaction;
import app.db.DB;
import app.transaction.TransactionController;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class AccountSummaryController {

    @FXML
    TextField accountTxt;

    @FXML
    VBox accountBox;

    @FXML
    private void initialize(){

    }

    public void setAccount(Account account) {
        if(account.getName() == null){
            accountTxt.setText(account.getBankNr());
        }else {
            accountTxt.setText(account.getName());
        }
    }

}
