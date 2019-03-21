package app.transaction;

import app.Entities.Transaction;
import app.db.DB;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class Transact {

    public static void makeTransaction(Transaction transaction){
        String sql ="INSERT INTO transactions (fromAccount, toAccount, amount, message, status, date) VALUES ('"
                +transaction.getFromAccount()+"', '"
                +transaction.getToAccount()+"', "
                +transaction.getAmount()+", '"
                +transaction.getMessage()+"', '"
                +transaction.getStatus()+"', '"
                +transaction.getDate()
                +"')";

        System.out.println(sql);
    }

    private void addMoney(String toAccount){

    }

    private void subtractMoney(String fromAccount){

    }


}
