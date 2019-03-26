package app.db;

import app.Entities.Account;
import app.Entities.Transaction;
import app.Entities.User;
import app.accountFunctions.AccountFunction;
import app.home.HomeController;
import app.login.LoginController;

import java.sql.*;
import java.time.LocalDate;
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

    public static Account getAccount(String bankNr){
        Account account = null;
        PreparedStatement ps = prep("SELECT * FROM accounts WHERE bankNr = ?");
        try {
            ps.setString(1, bankNr);
            account = (Account) new ObjectMapper<>(Account.class).mapOne(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }
        return account;
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

    public static boolean alreadyPlannedSalaryTransactions(String bankNr){
        List<Transaction> result = null;
        PreparedStatement ps = prep("SELECT * FROM plannedTransactions WHERE toAccount = ? AND message = 'Löneinsättning';");
        try {
            ps.setString(1, bankNr);
            result = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(ps.executeQuery());
        } catch (Exception e) { e.printStackTrace(); }

        if (result.size() == 0){
            return false;
        }else{ return true;}
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

    public static void makeSalaryTransaction(Account account){
        //finns det redan en lön registrerad?
        if(alreadyPlannedSalaryTransactions(account.getBankNr())){
            AccountFunction.displayConfirmBox("Det finns redan en lön registrerad, vad sägs som att du väntar till nästa månad med att få mer lön?");
        }else {
            LocalDate date = LocalDate.now().withDayOfMonth(25);
            Date sqlDate = Date.valueOf(date);
            Transaction transaction = new Transaction(
                    null,
                    account.getBankNr(),
                    25000,
                    "Löneinsättning",
                    sqlDate
            );
            planTransaction(transaction);
            AccountFunction.displayConfirmBox("Lönetransaktion registrerad");
        }
    }

    public static void makeCardTransaction(Account account, double amount){
        LocalDate date = LocalDate.now();
        Date sqlDate = Date.valueOf(date);

        Transaction transaction = new Transaction(
                account.getBankNr(),
                null,
                amount,
                "Kortbetalning",
                sqlDate
        );
        makeTransaction(transaction);
        AccountFunction.displayConfirmBox("Kortbetalning registrerad");
    }

    public static void upDateAccount(Account account, String column, String value){
        PreparedStatement ps = prep("UPDATE accounts SET "+column+" = ? WHERE bankNr = ?;");
        try {
            ps.setString(1, value);
            ps.setString(2, account.getBankNr());

        } catch (Exception e) { e.printStackTrace(); }
        DB.executeUpdate(ps);
        AccountFunction.displayConfirmBox("Konto uppdaterat");
    }

    public static void makeTransaction(Transaction transaction){
        //if there is enough money on account, make the transaction
        if(subtractMoney(transaction.getFromAccount(), transaction.getAmount())) {
            addMoney(transaction.getToAccount(), transaction.getAmount());

            //System.out.println("redo att göra transaktion datum är "+transaction.getDate());
            PreparedStatement statement = prep("INSERT INTO transactions (fromAccount, toAccount, amount, message, date) VALUES (?,?,?,?,?)");
            try {
                statement.setString(1,transaction.getFromAccount() );
                statement.setString(2,transaction.getToAccount() );
                statement.setDouble(3,transaction.getAmount() );
                statement.setString(4,transaction.getMessage() );
                statement.setDate(5,transaction.getDate() );
            } catch (SQLException e) {
                e.printStackTrace();
            }

            //System.out.println("statment redo datum är "+transaction.getDate());
            //System.out.println(statement);
            executeUpdate(statement);
            AccountFunction.displayConfirmBox("Transaktion genomförd");

        }else{
            HomeController.addBankMessage("Nedan planerad överföring kunde inte genomföras\n"+
                    transaction
            );
        }
    }

    public static void planTransaction(Transaction transaction){
        PreparedStatement statement = prep("INSERT INTO plannedTransactions (fromAccount, toAccount, amount, message, date) VALUES (?,?,?,?,?)");
        try {
            statement.setString(1,transaction.getFromAccount() );
            statement.setString(2,transaction.getToAccount() );
            statement.setDouble(3,transaction.getAmount() );
            statement.setString(4,transaction.getMessage() );
            statement.setDate(5,transaction.getDate() );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeUpdate(statement);

    }

    public static void deletePlannedTransaction(Transaction transaction){
        PreparedStatement statement = prep("DELETE FROM plannedTransactions WHERE id= ?");
        try {
            statement.setLong(1,transaction.getID() );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeUpdate(statement);
    }

    private static double getBalance(String account){
        double balance;
        ResultSet rs = executeQuery("SELECT balance FROM accounts WHERE bankNR='"+account+"'");
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
        //tillåt toAccount att vara null så vi kan göra ex kortköp då de inte skickas nånstans
        if (toAccount == null){
            return;
        }else {
            double newBalance, balance;
            balance = getBalance(toAccount);
            newBalance = balance + amount;
            setNewBalance(newBalance, toAccount);
        }
    }

    private static boolean subtractMoney(String fromAccount, double amount){
        //tillåt fromAccount att vara null så vi kan göra ex lönetransaktioner då de inte kommer nånstans ifrån
        if (fromAccount == null){
            return true;
        }else {
            double newBalance, balance;
            balance = getBalance(fromAccount);
            newBalance = balance - amount;

            if (newBalance >= 0) {
                setNewBalance(newBalance, fromAccount);
                return true;
            } else {
                return false;
            }
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
        executeUpdate(statement);
    }

    public static Account accountHasFunction(String function){
        List<Account> accounts = DB.getAccounts(LoginController.getUser().getSocialNo());

        for(Account account: accounts){
            if(account.getFunction() != null && account.getFunction().equals(function)){
                return account;
            }
        }
        return null;
    }

    public static void addAccount(String name, String function){
        PreparedStatement statement = prep("INSERT INTO accounts VALUES (?,?,?,?,?)");
        try {
            statement.setString(1, generateBankNr());
            statement.setString(2, name );
            statement.setString(3, function);
            statement.setString(4, LoginController.getUser().getSocialNo() );
            statement.setDouble(5,0 );
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeUpdate(statement);
        AccountFunction.displayConfirmBox("Konto tillagt");
    }

    public static void deleteAccount(String bankNr){
        PreparedStatement statement = prep("DELETE FROM accounts WHERE bankNr= ?;");
        try {
            statement.setString(1, bankNr );

        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeUpdate(statement);
        AccountFunction.displayConfirmBox("Konto borttaget");

    }

    private static String generateBankNr(){
        String bankNr;
        do {
            bankNr="";
            for(int i = 0; i < 10; i++ ){
                bankNr+= (int)Math.ceil(Math.random() * 9);
            }
        }while (getAccount(bankNr) != null);
        return bankNr;
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