package app.transfer;
import app.Entities.Transaction;
import app.db.DB;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Transfer {

    public static void makeTransaction(Transaction transaction){
        //if there is enough money on account, make the transaction
        if(subtractMoney(transaction.getFromAccount(), transaction.getAmount())) {
            addMoney(transaction.getToAccount(), transaction.getAmount());

            PreparedStatement statement = DB.prep("INSERT INTO transactions (fromAccount, toAccount, amount, message, status, date) VALUES (?,?,?,?,?,?)");
            try {
                statement.setString(1,transaction.getFromAccount() );
                statement.setString(2,transaction.getToAccount() );
                statement.setDouble(3,transaction.getAmount() );
                statement.setString(4,transaction.getMessage() );
                statement.setString(5,transaction.getStatus() );
                statement.setDate(6,transaction.getDate() );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            DB.executeUpdate(statement);

        }
    }

    private static double getBalance(String account){
        double balance;
        ResultSet rs = DB.executeQuery("SELECT balance FROM accounts WHERE bankNR='"+account+"'");
        try {
            rs.next();
            balance = rs.getDouble("balance");
            return balance;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void addMoney(String toAccount, double amount){
        double newBalance, balance;
        balance = getBalance(toAccount);
        newBalance= balance + amount;
        setNewBalance(newBalance, toAccount);
    }


    private static boolean subtractMoney(String fromAccount, double amount){
        double newBalance, balance;
        balance = getBalance(fromAccount);
        newBalance= balance - amount;

        if (newBalance >= 0){
            setNewBalance(newBalance, fromAccount);
            return true;
        }else {
            return false;
        }
    }

    private static void setNewBalance(double newBalance, String account){
        String sql = "UPDATE accounts SET balance= ? WHERE bankNr = ?";
        PreparedStatement statement  = DB.prep(sql);
        try {
            statement.setDouble(1, newBalance);
            statement.setString(2, account);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        DB.executeUpdate(statement);
    }


}
