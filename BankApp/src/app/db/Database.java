package app.db;

import app.Entities.Transaction;

import java.sql.*;
import java.util.HashMap;
import java.util.List;

public class Database {
    private static Database ourInstance = new Database();
    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        connectToDb();

        Thread checker = new Thread(this::checkForPlannedTransactions, "checker");
        checker.setDaemon(true);
        checker.start();
    }

    final String connectionURL = "jdbc:mysql://localhost/bankApp?user=root&password=hej&serverTimezone=UTC ";
    private Connection conn = null;
    private HashMap<String, PreparedStatement> preparedStatements = new HashMap<>();


    /** Returns a cached PreparedStatement if possible, else caches it for future use */
    public PreparedStatement prepareStatement(String SQLQuery){
        PreparedStatement ps = preparedStatements.get(SQLQuery);
        if (ps == null) {
            try { ps = conn.prepareStatement(SQLQuery); }
            catch (SQLException e) { e.printStackTrace(); }
        }
        return ps;
    }

    private void connectToDb(){
        try { conn = DriverManager.getConnection(connectionURL);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public  ResultSet executeQuery(PreparedStatement statement) {
        try {
            ResultSet result = statement.executeQuery();
            return result;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void executeUpdate(PreparedStatement statement) {
        try {
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkForPlannedTransactions(){
        List<Transaction> plannedTransactions  = null;
        PreparedStatement statement = prepareStatement("SELECT * FROM plannedTransactions WHERE date <= CURDATE();");

        while(true){
            try {
                plannedTransactions = (List<Transaction>)(List<?>)new ObjectMapper<>(Transaction.class).map(statement.executeQuery());
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (plannedTransactions != null){
                for (Transaction transaction: plannedTransactions){
                    DB.makeTransaction(transaction);
                    DB.deletePlannedTransaction(transaction);
                }
            }

            try {
                Thread.sleep(600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

