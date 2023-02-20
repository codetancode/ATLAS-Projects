package dbTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
//import com.mysql.jdbc.Driver;

/**
 * The class ConnectionManager is a Singleton implementation which controls single-point creation of
 * a connection object from a DB.
 * It sends the connection instance to other classes of the Lower Layer/DB Layer/dbTools Package.
 * Only Lower Layer classes interact with this class to get connection instance.
 **/

public class ConnectionManager {

  private static Connection con;

  public ConnectionManager() { }

  public static Connection getConnection() throws SQLException, ClassNotFoundException {
    try {
      //to check if JDBC Driver exist as dependency JAR or not
      Class.forName("com.mysql.cj.jdbc.Driver");
      con = DriverManager.getConnection("jdbc:mysql://localhost:3306/buspass_app?serverTimezone=UTC",
              "root", "");
      return con;
    } catch (Exception e) {
      System.out.println("Connection Issue Found");
      System.out.println("Please See if the Mysql server is running on port 3306, and has database 'buspass_app' !!");
      e.printStackTrace();
      throw e;
    }
  }
}

