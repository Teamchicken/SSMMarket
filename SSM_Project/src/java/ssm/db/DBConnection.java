package ssm.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
  public static Connection getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3000;databaseName=ssmmarket", "root", "admin");
      return conn;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
