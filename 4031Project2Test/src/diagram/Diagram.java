package diagram;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import rendering.MainScene;
import rendering.SceneManager.RenderableScene;
import rendering.SceneManager.SceneManager;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import diagram.Diagram;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

//import com.sun.prism.paint.Color;
import javafx.scene.paint.Color;

import application.Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Stack;

public class Diagram {

	private Text QueryText, ShowQueryText, BoxText;
	private TextArea QueryTextArea;
	private HBox QueryBox, NodeBox;
	private VBox DiagramBox;
	private Rectangle BoxRectangle;
	private Group QueryArea;
	private Button QueryButton;
	private ScrollPane Scroll;
	private Line Line;

	// private final String url = "jdbc:postgresql://localhost/TPC-H";
	private final String url = "jdbc:postgresql://localhost/CZ4031_Project1";
	private final String user = "postgres";
	private final String password = "150994";

	private Stack<String> Queries = new Stack<String>();
	private String Result = new String();

	public void init(Group root) {
		
		DiagramBox = new VBox();
		DiagramBox.setPrefWidth(300);
		DiagramBox.setPrefHeight(800);

		
//		ScrollPane scrollPane = new ScrollPane();
//		 scrollPane.setContent(DiagramBox);
//		 scrollPane.setFitToWidth(true);
//		 scrollPane.setOpacity(1);
//		 scrollPane.setTranslateX(SceneManager.scaledX(460));
//		 scrollPane.setTranslateY(SceneManager.scaledY(0));
		 

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
			connect();
			int i = 1;
			int j = 1;

			Result = Result.replaceAll("\"|,", "");
			Result = Result.replaceAll("[\\[\\](){}]", "");
			String[] ResultArray = Result.split("Node Type:");

			for (String results : ResultArray) {
				Queries.push(results);
				System.out.println("Sentence: " + i + " " + results);
				i++;
			}

			while (!Queries.isEmpty()) {
				String steps = new String();
				steps = Queries.pop();
				String remove1 = new String("Plans:");
				String remove2 = new String("Plan:");
				steps = steps.replace(remove1, "");
				steps = steps.replace(remove2, "");
				steps = steps.split("Planning Time")[0];
				steps = steps.replaceAll("\\s+$", "");

				// System.out.println("Step " + j + ": " + steps);

				String stepNode = new String();
				String stepDetail = new String();
				String array[] = steps.split("\n", 2);
				stepNode = array[0];

				if (stepNode.isEmpty()) {
					break;
				}

				stepDetail = steps.replace(stepNode, "");
				stepDetail.replaceAll("^\\s+", "");
				System.out.println("StepDetail " + j + ": " + stepDetail);

				System.out.println("StepNode " + j + ":" + stepNode);

				StackPane stack = new StackPane();

				BoxRectangle = new Rectangle(0, 0, 100, 50);
				BoxRectangle.setFill(Color.TRANSPARENT);
				BoxRectangle.setStroke(Color.BLACK);

				BoxText = new Text(stepNode);

				Tooltip.install(BoxRectangle, new Tooltip(stepDetail));
				stack.getChildren().addAll(BoxRectangle, BoxText);

				NodeBox = new HBox();
				NodeBox.getChildren().addAll(stack);
				NodeBox.setTranslateX(SceneManager.scaledX(460));
				NodeBox.setTranslateY(SceneManager.scaledY(0));

				if (j != (i - 2)) {
					Line = new Line();
					Line.setStartX(0.0f);
					Line.setStartY(0.0f);
					Line.setEndX(0.0f);
					Line.setEndY(50.0f);
					Line.setTranslateX(SceneManager.scaledX(530));
					Line.setTranslateY(SceneManager.scaledY(0));
					DiagramBox.getChildren().addAll(NodeBox, Line);
				} else {
					DiagramBox.getChildren().add(NodeBox);
				}
				j++;
			}

			 
			// scrollPane.setPrefSize(800, 3000);
//			scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
//			scrollPane.setPannable(true); // allow scrolling via mouse
	

			root.getChildren().add(DiagramBox);

		});
		
		ShowQueryText = new Text();
		ShowQueryText.setId("ShowQueryText");
//		ShowQueryText.setText(QueryTextArea.getText());
		ShowQueryText.setStyle("-fx-font: 20 calibri;");
		ShowQueryText.setTranslateX(SceneManager.scaledX(0));
		ShowQueryText.setTranslateY(SceneManager.scaledY(500));
		
		QueryBox = new HBox();
		QueryBox.getChildren().addAll(QueryText, QueryTextArea, QueryButton);
		QueryBox.setTranslateX(SceneManager.scaledX(80));
		QueryBox.setTranslateY(SceneManager.scaledY(50));

		QueryArea = new Group();
		QueryArea.getChildren().add(QueryBox);
		QueryArea.setTranslateX(SceneManager.scaledX(1000));
		QueryArea.setTranslateY(SceneManager.scaledY(0));

		root.getChildren().add(QueryArea);
	}

	public Connection connect() {
		Connection conn = null;
		Statement query = null;
		try {
			conn = DriverManager.getConnection(url, user, password);
			System.out.println("Connected to the PostgreSQL server successfully.");

			query = conn.createStatement();

			// input query
			// System.out.println("Enter your query: ");
			// Scanner scanner = new Scanner(System.in);
			String sql = "EXPLAIN (ANALYZE true, COSTS true, FORMAT json)" + QueryTextArea.getText() + ";";
			// String sql = "EXPLAIN (ANALYZE true, COSTS true, FORMAT json)" +
			// QueryTextArea.getText() + ";";

			ResultSet rs = query.executeQuery(sql);

			// printing of region table
			System.out.println("postgresqlQEP");
			while (rs.next()) {
				String postgresqlQEP = rs.getString("QUERY PLAN");
				System.out.println(postgresqlQEP);
				Result = postgresqlQEP;
				// Queries.push(postgresqlQEP);
			}

			rs.close();
			query.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}
}
