package app;

import app.db.DB;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class bullshit {

    public bullshit(){
        String sql = "SELECT bankNr FROM accounts";
        ResultSet result = DB.executeQuery(sql);
        List socialNo= resultSetToArrayList(result);


    }

    public void addTransactions(List socialNo){
        for (int i = 0; i< 1000)
    }

    public void gettogether(ResultSet rs, List socialNumbers){
        int i = 1;
        Object socialNo;
        int random= random(4);
        try {
            while (rs.next() && i < 103) {
                socialNo= socialNumbers.get(i);
                String sql = "UPDATE accounts SET user = '"+socialNo+"' where bankNr = '"+rs.getString("bankNr")+"'";
                random--;
                if (random == 0){
                    random= random(4);
                    i++;
                }
                DB.updateTable(sql);
                System.out.println(sql);
            }
        } catch (Exception ex) { ex.printStackTrace(); }

    }

    public int random(int i){
        int rand = (int) Math.ceil(Math.random() * i);
        return rand;
    }
    public List resultSetToArrayList(ResultSet rs){
        ArrayList list = new ArrayList(500);
        try {
            while (rs.next()) {
                list.add(rs.getObject(1));
            }
        } catch (Exception ex) { ex.printStackTrace(); }

        return list;
    }

}
