import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class clientHandler {

    private String url;

    private String path;

    private String fileName;

    public static List<CoronaDataObject> coronaObjects;

    public clientHandler(String url,String path, String fileName) {
        this.url = url;
        this.coronaObjects = new ArrayList<CoronaDataObject>();
        this.path = path;
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static List<CoronaDataObject> getCoronaObjects() {
        return coronaObjects;
    }

    public static void setCoronaObjects(List<CoronaDataObject> coronaObjects) {
        clientHandler.coronaObjects = coronaObjects;
    }

    private void makeObjects(String data) {
        try{
            StringReader csvBodyReader = new StringReader(data);
            CoronaDataObject obj;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(csvBodyReader);
            for (CSVRecord record : records) {
                date = dateFormat.parse(record.get("Last_Update"));
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                obj = new CoronaDataObject(record.get("Province_State"), record.get("Country_Region"),
                        sqlDate,Float.parseFloat(record.get("Confirmed") != "" ? record.get("Confirmed") : "0.0" ),
                        Float.parseFloat(record.get("Deaths") != "" ? record.get("Deaths") : "0.0"),
                        Float.parseFloat(record.get("Recovered") != "" ? record.get("Recovered") : "0.0")
                        ,Float.parseFloat(record.get("Active") != "" ? record.get("Active") : "0.0"),
                        Float.parseFloat(record.get("Total_Test_Results") != "" ? record.get("Total_Test_Results") : "0.0"));
                clientHandler.coronaObjects.add(obj);
            }
        }
        catch(IOException | ParseException e){
            e.printStackTrace();
        }

    }

    public void displayObjects(){
        for (int x = 0;x<this.coronaObjects.size();x++){
            this.coronaObjects.get(x).display();
        }
    }

    public String getData(){
        try{
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest req = HttpRequest.newBuilder().uri(URI.create(this.url)).build();
            HttpResponse<String> httpResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
            String data = httpResponse.body();
            makeObjects(data);
            return data;
        }
        catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
        return "";
    }

    public void displayFile(){
        FileHandler cFh = new coronaFileHandler("/","Corona_Cases_in_US_By_State.txt");
        cFh.displayFile();
    }

    public void writeToFile(){
        FileHandler cFh = new coronaFileHandler("/","Corona_Cases_in_US_By_State.txt");
        cFh.writeFile();

    }

    public String displayAllStates(DbHandler db){
        ResultSet rs = db.findByCountry("US");
        return convertToString(rs);

    }

    public String displaySingleState(String choice, DbHandler db){
        ResultSet rs = db.findByState(choice);
        return convertToString(rs);
    }

    public String convertToString(ResultSet rs){
        try{

            String choice = "<h1>" + "Province/State " + "Country "
                    + "Last Update " + "Confirmed " + "Deaths " + "Recovered " +
                    "Active " + "Total Test Result" + "<h1>";
            while(rs.next()){
                choice = choice + "<h1>" + rs.getString(1) + " " + rs.getString(2)
                        + " " + rs.getString(3) + " " + rs.getString(4)
                        + " " + rs.getString(5)
                        + " " + rs.getString(6) + " " + rs.getString(7)
                        + " " + rs.getString(8) + " " + rs.getString(9) + "<h1>";
            }
            return choice;
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String toString() {
        return "clientHandler{" +
                "url='" + url + '\'' +
                '}';
    }


}
