package app.db;

import java.sql.*;
import java.util.HashMap;

public class Database {
    private static Database ourInstance = new Database();
    public static Database getInstance() {
        return ourInstance;
    }
    private Database() { connectToDb(); }


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

    public  void updateTable(String sql) {
        try {
            Statement statement= conn.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

