import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class hello {
    public static String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/01-01-2021.csv";
    public static final Logger logHello = LoggerFactory.getLogger("hello");
    public static String apiData;
    public static clientHandler ClientHandler;
    public static DbHandler db;

    public static void main(String[] args) {

        ClientHandler = new clientHandler(hello.url,"/","COVID19_US.txt");
        ClientHandler.getData();
        db = new coronaDbHandler(null,"demo","postgres","aps123");
        db.makeTable("corona_data");
        db.fillTable();
        runServer();
        Thread t1 = new Thread(new Runnable(){
                @Override
                public void run() {
                    String choice = "";
                    Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                    System.out.println("Welcome to COVID_19 US Data Dump! \n Please choose an" +
                            "option below to continue");
                    while(! choice.equals("3"))
                    {
                        System.out.println("Enter 1 to see data from all states");
                        System.out.println("Enter 2 to select a state");
                        System.out.println("Enter 3 to exit!");
                        choice = myObj.nextLine();  // Read user input
                        if(choice.equals("1")){
                            apiData = ClientHandler.displayAllStates(db);
                        }
                        else if(choice.equals("2")){
                            System.out.println("Enter name of the State");
                            choice = myObj.nextLine();
                            String dataState;
                            dataState = ClientHandler.displaySingleState(choice,db);
                            if(dataState.equals("")){
                                System.out.println("Data for entered state does not exist!");
                            }
                            else{
                                apiData = dataState;
                            }
                        }
                    }
                }
            });
        logHello.info("Server starting!");
        t1.start();
        logHello.error("Server has crashed Stopped!");

    }

    public static void runServer() {
        Tomcat server = new Tomcat();
        server.setBaseDir(System.getProperty("java.io.tmpdir"));
        server.setPort(8080);
        server.getConnector();
        server.addContext("", null);
        server.addServlet("", "defaultServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                resp.getWriter().println("Hello!");
            }
        }).addMapping("/*");

        server.addServlet("", "helloServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {

                resp.getWriter().println(apiData);

                /*ClientHandler.displayObjects();
                ClientHandler.writeToFile();
                ClientHandler.displayFile();*/
            }
        }).addMapping("/hello");
        try {
           server.start();
           //server.getServer().await();

        } catch (LifecycleException e) {
            // TODO Auto-generated catch block
            logHello.error("Life Cycle Exception!");
            e.printStackTrace();
        }

    }
}
