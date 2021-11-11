import java.sql.Connection;
import java.sql.ResultSet;

public class coronaDbHandler extends DbHandler{


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
    ResultSet findByCountry(String country) {
        return null;
    }
}
