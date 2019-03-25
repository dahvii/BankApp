package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/** A Helper class for interacting with the Database using short-commands */
public abstract class DB {

    public static PreparedStatement prep(String SQLQuery){
        return Database.getInstance().prepareStatement(SQLQuery);
    }

    public static User getMatchingUser(String socialNo, String password){
        User result = null;
        PreparedStatement ps = prep("SELECT * FROM users WHERE socialNo = ? AND password = ?");
        try {
            ps.setString(1, socialNo);
            ps.setString(2, password);
            result = (User)new ObjectMapper<>(User.class).mapOne(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }

    public static ResultSet executeQuery(String SQLQuery){
        ResultSet result = Database.getInstance().executeQuery(prep(SQLQuery));
        return result;
    }

    public static void executeUpdate(PreparedStatement statement) {
        Database.getInstance().executeUpdate(statement);
    }

    //hämtar transaktioner för ett visst konto
    public static List<Transaction> getAccountTransactions(String bankNr, int offset, int limit){
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE fromAccount = ? OR toAccount = ? ORDER BY date DESC LIMIT ? OFFSET ?");
        try {
            ps.setString(1, bankNr);
            ps.setString(2, bankNr);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }


    //hämtar alla transaktioner från en viss användare
    public static List<Transaction> getUserTransactions(String socialNo, int offset, int limit){
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE fromAccount IN " +
                "(SELECT bankNr from accounts WHERE user = ?) " +
                "OR toAccount IN (SELECT bankNr from accounts WHERE user = ?)" +
                " ORDER BY date DESC LIMIT ? OFFSET ?;");
        try {
            ps.setString(1, socialNo);
            ps.setString(2, socialNo);
            ps.setInt(3, limit);
            ps.setInt(4, offset);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }


    //for testing
    public static List<Transaction> getTransactionsBetweenOwnAccounts(String socialNo){
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM transactions WHERE fromAccount IN " +
                "(SELECT bankNr from accounts WHERE user = ?) " +
                "AND toAccount IN (SELECT bankNr from accounts WHERE user = ?)" +
                " ORDER BY date DESC;");
        try {
            ps.setString(1, socialNo);
            ps.setString(2, socialNo);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result;
    }

    public static List<Account> getAccounts(String socialNo){
        List<Account> result = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE user = ?");
        try {
            ps.setString(1, socialNo);
            result = (List<Account>)(List<?>)new ObjectMapper<>(Account.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }

  /*
        Example method with default parameters
    public static List<Transaction> getTransactions(int accountId){ return getTransactions(accountId, 0, 10); }
    public static List<Transaction> getTransactions(int accountId, int offset){ return getTransactions(accountId, offset, offset + 10); }
    public static List<Transaction> getTransactions(int accountId, int offset, int limit){
        List<Transaction> result = null;
        PreparedStatement ps = prep("bla bla from transactions WHERE account-id = "+accountId+" OFFSET "+offset+" LIMIT "+limit);
        try {
            result = (List<Transaction>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return result; // return User;
    }
    */
}