package diagram;

import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import rendering.MainScene;
import rendering.SceneManager.RenderableScene;
import rendering.SceneManager.SceneManager;
import javafx.application.Application;
import javafx.scene.Group;
import diagram.Diagram;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Stack;


public class Diagram{
	
	private Text QueryText, LineText, BoxText;
	private TextArea QueryTextArea;
	private HBox QueryBox, DiagramBox;
	private Rectangle BoxRectangle;
	private Group QueryArea;
	private Button QueryButton;
	
	public void init(Group root){
	QueryText = new Text();
	QueryText.setId("QueryText");
	QueryText.setText("Enter query:");
	QueryText.setStyle("-fx-font: 20 calibri;");
	QueryText.setTranslateX(SceneManager.scaledX(0));
	QueryText.setTranslateY(SceneManager.scaledY(0));
    
    QueryTextArea = new TextArea();
    QueryTextArea.setId("QueryText");
    QueryTextArea.setWrapText(true);
    QueryTextArea.setPrefColumnCount(35);
    QueryTextArea.setPrefRowCount(15);
	QueryTextArea.setTranslateX(SceneManager.scaledX(-130));
	QueryTextArea.setTranslateY(SceneManager.scaledY(50));
	
	QueryButton = new Button("Submit");
	QueryButton.setScaleX(SceneManager.scaledX(2));
	QueryButton.setScaleY(SceneManager.scaledY(2));
	QueryButton.setTranslateX(SceneManager.scaledX(-50));
	QueryButton.setTranslateY(SceneManager.scaledX(200));
	QueryButton.setOnAction(actionEvent -> {
		
	});
	
	QueryBox = new HBox();
	QueryBox.getChildren().addAll(QueryText, QueryTextArea, QueryButton);
	QueryBox.setTranslateX(SceneManager.scaledX(80));
	QueryBox.setTranslateY(SceneManager.scaledY(50));
	
	QueryArea = new Group();
	QueryArea.getChildren().add(QueryBox);
	QueryArea.setTranslateX(SceneManager.scaledX(1000));
	QueryArea.setTranslateY(SceneManager.scaledY(0));

	root.getChildren().add(QueryArea);
	
	DiagramBox = new HBox();

	}
	
	private final String url = "jdbc:postgresql://localhost/TPC-H";
//  private final String url = "jdbc:postgresql://localhost/CZ4031_Project1";
    private final String user = "postgres";
    private final String password = "150994";

  /**
   * Connect to the PostgreSQL database
   *
   * @return a Connection object
   */
  public Connection connect() {
  Connection conn = null;
	Statement query = null;
	Stack stack = new Stack();
	try {
	    conn = DriverManager.getConnection(url, user, password);
	    System.out.println("Connected to the PostgreSQL server successfully.");

	    query = conn.createStatement();

	    // input query
	    System.out.println("Enter your query: ");
	    Scanner scanner = new Scanner(System.in);
	    String sql = "EXPLAIN (ANALYZE true, COSTS true, FORMAT json)" + scanner.nextLine() + ";";
	    // System.out.println("Your sql is " + sql);

	    ResultSet rs = query.executeQuery(sql);

	    // printing of region table
	    System.out.println("postgresqlQEP");
	    while (rs.next()) {
		String postgresqlQEP = rs.getString("QUERY PLAN");
		System.out.println(postgresqlQEP);

	    }

	    rs.close();
	    query.close();
	    conn.close();

	} catch (SQLException e) {
	    System.out.println(e.getMessage());
	}

	return conn;
  }

  /**
   * @param args
   *            the command line arguments
   */
  public static void main(String[] args) {
		Diagram app = new Diagram();
		app.connect();
	    }
	
}
