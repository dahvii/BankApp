package app.transaction;
import app.Entities.Transaction;
import app.db.DB;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Transact {

    public static void makeTransaction(Transaction transaction){
        //if there is enough money on account, make the transaction
        if(subtractMoney(transaction.getFromAccount(), transaction.getAmount())) {
            addMoney(transaction.getToAccount(), transaction.getAmount());

            String sql = "INSERT INTO transactions (fromAccount, toAccount, amount, message, status, date) VALUES ('"
                    + transaction.getFromAccount() + "', '"
                    + transaction.getToAccount() + "', "
                    + transaction.getAmount() + ", '"
                    + transaction.getMessage() + "', '"
                    + transaction.getStatus() + "', '"
                    + transaction.getDate()
                    + "')";

            ResultSet rs = DB.updateTable(sql);
            try {
                Long id = rs.getLong("GENERATED_KEY");
            } catch (SQLException e) {
                e.printStackTrace();
            }

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
        String sql = "UPDATE accounts SET balance= "+newBalance+" WHERE bankNr = '"+account+"'";

        //DB.updateTable(sql);
        System.out.println(sql);

    }


}
