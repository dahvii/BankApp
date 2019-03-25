package app.transaction;


import app.Entities.Account;
import app.Entities.Transaction;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class TransactionController {

    @FXML Label message;
    @FXML Label amount;
    @FXML Label date;
    @FXML Label betweenOwnAccountsLbl;
    @FXML Label fromAccount;
    @FXML Label toAccount;


    @FXML
    private void initialize(){
        betweenOwnAccountsLbl.setVisible(false);
    }

    public void setTransaction(Transaction transaction, List<Account> accounts) {
        //är summan dragen eller tillagd från/till något av användarens konton?
        for(Account account: accounts){
            if(account.getBankNr().equals(transaction.getFromAccount())){
                amount.setText("-"+transaction.getAmount());
            }else if (account.getBankNr().equals(transaction.getToAccount())) {
                amount.setText("+"+transaction.getAmount());
            }

            //är transaktionen gjord mellan två egna konton?
            if (isBetweenOwnAccounts(transaction, accounts)) {
                betweenOwnAccountsLbl.setVisible(true);
                amount.setText("+/-"+transaction.getAmount());
                break;
            }
        }
        message.setText(transaction.getMessage());
        date.setText(""+transaction.getDate());

        fromAccount.setText("från konto: "+transaction.getFromAccount());
        toAccount.setText("till konto: "+ transaction.getToAccount());
    }
    public boolean isBetweenOwnAccounts(Transaction transaction, List<Account> accounts) {

        for(Account account: accounts){
            if(account.getBankNr().equals(transaction.getFromAccount())){
                for(Account toAccount: accounts) {
                    if (toAccount.getBankNr().equals(transaction.getToAccount())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    public void setTransaction(Transaction transaction, Account account) {

        if(account.getBankNr().equals(transaction.getFromAccount())){
            amount.setText("-"+transaction.getAmount());

        }else{
            amount.setText("+"+transaction.getAmount());

        }

        message.setText(transaction.getMessage());
        date.setText(""+transaction.getDate());
    }
}
