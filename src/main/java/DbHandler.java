import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class DbHandler {
    protected Connection conn;
    protected String dbName;
    protected String userName;
    protected String pass;

    public DbHandler(Connection conn, String dbName, String userName, String pass) {
        this.conn = conn;
        this.dbName = dbName;
        this.userName = userName;
        this.pass = pass;
        this.makeConnection();
    }
    void makeConnection(){
        String url = "jdbc:postgresql://localhost:5432/demo";
        try {
            Connection conn = DriverManager.getConnection(url,  this.userName, this.pass);
            this.conn = conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    abstract public Connection getConn();

    abstract public void setConn(Connection conn);

    abstract public String getDbName();

    abstract public void setDbName(String dbName);

    abstract public String getUserName();

    abstract public void setUserName(String userName);

    abstract public String getPass();

    abstract public void setPass(String pass);

    abstract int makeTable(String name);

    abstract void fillTable();

    abstract int deleteTable();

    abstract int addRow(String table, String data);

    abstract ResultSet findById(String id);

    abstract ResultSet findByCountry(String country);

    abstract ResultSet findByState(String state);

}
