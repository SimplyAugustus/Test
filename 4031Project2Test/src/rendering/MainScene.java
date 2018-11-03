package rendering;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import config.AppVar;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rendering.SceneManager.RenderableScene;
import rendering.SceneManager.SceneManager;
import diagram.Diagram;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.Scanner;
//import java.util.Stack;

public class MainScene extends RenderableScene{
	
	private Diagram Diagram;
	
//    private final String url = "jdbc:postgresql://localhost/TPC-H";
////    private final String url = "jdbc:postgresql://localhost/CZ4031_Project1";
//    private final String user = "postgres";
//    private final String password = "150994";
//
//    /**
//     * Connect to the PostgreSQL database
//     *
//     * @return a Connection object
//     */
//    public Connection connect() {
//    Connection conn = null;
//	Statement query = null;
//	Stack stack = new Stack();
//	try {
//	    conn = DriverManager.getConnection(url, user, password);
//	    System.out.println("Connected to the PostgreSQL server successfully.");
//
//	    query = conn.createStatement();
//
//	    // input query
//	    System.out.println("Enter your query: ");
//	    Scanner scanner = new Scanner(System.in);
//	    String sql = "EXPLAIN (ANALYZE true, COSTS true, FORMAT json)" + scanner.nextLine() + ";";
//	    // System.out.println("Your sql is " + sql);
//
//	    ResultSet rs = query.executeQuery(sql);
//
//	    // printing of region table
//	    System.out.println("postgresqlQEP");
//	    while (rs.next()) {
//		String postgresqlQEP = rs.getString("QUERY PLAN");
//		System.out.println(postgresqlQEP);
//
//	    }
//
//	    rs.close();
//	    query.close();
//	    conn.close();
//
//	} catch (SQLException e) {
//	    System.out.println(e.getMessage());
//	}
//
//	return conn;
//    }
//
//    /**
//     * @param args
//     *            the command line arguments
//     */
   
	@Override
	public void setupNode()
	{
		
		Diagram = new Diagram();
		Diagram.init(root);
		
	}

	@Override
	public void mouseUpdate(MouseEvent event) {
		
		
	}
	
//	 public static void main(String[] args) {
//			MainScene app = new MainScene();
//			app.connect();
//		    }


}
