package app.transaction;


import app.Entities.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;

    @FXML
    private void initialize(){
    }

    public void setTransaction(Transaction transaction) {
        message.setText(transaction.getMessage());
        amount.setText(""+transaction.getAmount());
        date.setText(""+transaction.getDate());
    }
}
