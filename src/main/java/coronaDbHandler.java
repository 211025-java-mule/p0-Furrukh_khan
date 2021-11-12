import javax.xml.transform.Result;
import java.sql.*;

public class coronaDbHandler extends DbHandler{

    String tableName;

    public coronaDbHandler(Connection conn, String dbName, String userName, String pass) {
        super(conn, dbName, userName, pass);
    }

    @Override
    public Connection getConn() {
        return super.conn;
    }

    @Override
    public void setConn(Connection conn) {
        super.conn = conn;
    }

    @Override
    public String getDbName() {
        return super.dbName;
    }

    @Override
    public void setDbName(String dbName) {
        super.dbName= dbName;
    }

    @Override
    public String getUserName() {
        return super.userName;
    }

    @Override
    public void setUserName(String userName) {
        super.userName = userName;
    }

    @Override
    public String getPass() {
        return super.pass;
    }

    @Override
    public void setPass(String pass) {
        super.pass = pass;
    }

    @Override
    int makeTable(String name) {
        try {
            this.tableName = name;
            String query = "Drop table if exists corona_data";
            PreparedStatement statement1 = super.conn.prepareStatement(query);
            statement1.executeUpdate();
            query = "Create table if not exists ";
            query = query + name;
            query = query + " (id serial primary key, province_state text, country_region text, last_update date, confirmed float, deaths float, recovered float, active float, total_test_results float)";
            PreparedStatement statement2 = super.conn.prepareStatement(query);
            statement2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    int deleteTable() {
        return 0;
    }

    @Override
    int addRow(String table, String data) {
        return 0;
    }

    @Override
    ResultSet findById(String id) {
        return null;
    }

    @Override
    public ResultSet findByState(String state) {
        try{
            String query = "select * from corona_data where province_state = \'"  + state + "\'";
            PreparedStatement statement = super.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return rs;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }


    @Override
    ResultSet findByCountry(String country) {
        try{
            String query = "select * from corona_data where country_region = \'"  + country + "\'";
            PreparedStatement statement = super.conn.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return rs;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void fillTable(){
        try{
            String query = "Insert into " + this.tableName + "(province_state,country_region,last_update," +
                    "confirmed,deaths,recovered,active,total_test_results)values(?,?,?,?,?,?,?,?)";
            PreparedStatement statement = null;
            for(int x = 0;x< clientHandler.coronaObjects.size();x++){
                statement = super.conn.prepareStatement(query);
                statement.setString(1,clientHandler.coronaObjects.get(x).getProvince_State());
                statement.setString(2,clientHandler.coronaObjects.get(x).getCountry_Region());
                statement.setDate(3, (Date) clientHandler.coronaObjects.get(x).getLast_Update());
                statement.setFloat(4,clientHandler.coronaObjects.get(x).getConfirmed());
                statement.setFloat(5,clientHandler.coronaObjects.get(x).getDeaths());
                statement.setFloat(6,clientHandler.coronaObjects.get(x).getRecovered());
                statement.setFloat(7,clientHandler.coronaObjects.get(x).getActive());
                statement.setFloat(8,clientHandler.coronaObjects.get(x).getTotal_Test_Results());
                statement.executeUpdate();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
}
