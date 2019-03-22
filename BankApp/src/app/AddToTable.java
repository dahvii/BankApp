package app;

import app.db.DB;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AddToTable {

    public AddToTable(){
/*        String sql = "SELECT bankNr FROM accounts";
        ResultSet result = DB.executeQuery(sql);
        List socialNo= resultSetToArrayList(result);
        addTransactions(socialNo);
*/



    }


    public void addTransactions(List socialNo){
        String sql, toAccount, fromAccount;
        int rand;
        for (int i = 0; i< 400; i++){
            do {
                fromAccount = (String) socialNo.get(random(400)-1);
                toAccount = (String) socialNo.get(random(400)-1);
            }while (toAccount.equals(fromAccount));


            sql="INSERT INTO transactions (fromAccount, toAccount) VALUES ('"+fromAccount+"', '"+toAccount+"');";
            PreparedStatement statement = DB.prep(sql);
            DB.executeUpdate(statement);

        }
    }

    public void updateTransactions(){
        File file = new File("/Users/elliotterking/Documents/Java Utbildning-18/MOCK_DATA2.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String line, amount, message, date;
        int first, last;
        int i = 1005;

        while (sc.hasNextLine()) {
            line = sc.nextLine();
            first = line.indexOf(",");
            last = line.lastIndexOf(",");
            amount = line.substring(0, first);
            message=line.substring(first+1, last);
            date= line.substring(last+1);

            String sql = "UPDATE transactions SET amount = "+amount+", message='"+message+"', date='"+date+"'  where id ="+i+";";
            PreparedStatement statement = DB.prep(sql);

            DB.executeUpdate(statement);
            i++;
        }
    }


    public void addSocialNoToBankNo(ResultSet rs, List socialNumbers){
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
                PreparedStatement statement= DB.prep(sql);
                DB.executeUpdate(statement);
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
