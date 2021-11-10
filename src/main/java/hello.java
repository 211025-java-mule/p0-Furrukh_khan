import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.tomcat.util.http.fileupload.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class hello {
    public static String url = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_daily_reports_us/01-01-2021.csv";
    public static final Logger logHello = LoggerFactory.getLogger("hello");


    public static void main(String[] args) {

        logHello.info("Server starting!");
        runServer();
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
                String fileName = req.getPathInfo().replaceFirst("/", "");
                InputStream file = getClass().getClassLoader().getResourceAsStream(fileName);
                String mimeType = getServletContext().getMimeType(fileName);
                resp.setContentType(mimeType);
                IOUtils.copy(file, resp.getOutputStream());
            }
        }).addMapping("/*");

        server.addServlet("", "helloServlet", new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                String data = req.getParameter("name");
                clientHandler ClientHandler = new clientHandler(hello.url,"/","COVID19_US.txt");
                if (data == null)
                    data = ClientHandler.getData();
                ClientHandler.displayObjects();
                ClientHandler.writeToFile();
                ClientHandler.displayFile();
                resp.getWriter().println("<h1>" + data + "</h1>");
            }
        }).addMapping("/hello");
        try {
           server.start();
           server.getServer().await();

        } catch (LifecycleException e) {
            // TODO Auto-generated catch block
            logHello.error("Life Cycle Exception!");
            e.printStackTrace();
        }

    }
}
