package diagram;

import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.scene.Group;
import rendering.SceneManager.SceneManager;
import diagram.Diagram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Stack;

import config.AppVar;


public class Diagram {

	private Text QueryText, ShowQueryText, BoxText, InstructionText;
	private TextArea QueryTextArea;
	private HBox QueryBox, NodeBox;
	private VBox DiagramBox;
	private Rectangle BoxRectangle;
	private Group QueryArea;
	private Button QueryButton, ResetButton;
	private ScrollPane Scroll;
	private Line Line;
	private String instructionText;

	// private final String url = "jdbc:postgresql://localhost/TPC-H";
	private final String url = "jdbc:postgresql://localhost/CZ4031_Project1";
	private final String user = "postgres";
	private final String password = "150994";

	private Stack<String> Queries = new Stack<String>();
	private String Result = new String();

	public void init(Group root) {
		
		instructionText = AppVar.getVar("instruction");
		
		DiagramBox = new VBox();
		
		ScrollPane scrollPane = new ScrollPane();
		scrollPane.setContent(DiagramBox);
		scrollPane.setFitToWidth(true);
		scrollPane.setTranslateX(SceneManager.scaledX(20));
		scrollPane.setPrefViewportWidth(780);
		scrollPane.setPrefViewportHeight(800);
		scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
		scrollPane.setPannable(true); 

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
		QueryButton.setTranslateX(SceneManager.scaledX(-830));
		QueryButton.setTranslateY(SceneManager.scaledY(150));
		QueryButton.setOnAction(actionEvent -> {
			
//			ShowQueryText.setText(QueryTextArea.getText());
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

				Tooltip.install(stack, new Tooltip(stepDetail));
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

			root.getChildren().add(scrollPane);
			
		});

		ResetButton = new Button("Reset");
		ResetButton.setScaleX(SceneManager.scaledX(2));
		ResetButton.setScaleY(SceneManager.scaledY(2));
		ResetButton.setTranslateX(SceneManager.scaledX(-896));
		ResetButton.setTranslateY(SceneManager.scaledY(250));
		ResetButton.setOnAction(actionEvent -> {
			root.getChildren().clear();
			init(root);
		});
		
		InstructionText = new Text(instructionText);
		InstructionText.setId("InstructionText");
		InstructionText.setStyle("-fx-font: 16 calibri;");
		InstructionText.setTranslateX(SceneManager.scaledX(-695));
		InstructionText.setTranslateY(SceneManager.scaledY(500));

//		ShowQueryText = new Text();
//		ShowQueryText.setId("ShowQueryText");
//		ShowQueryText.setStyle("-fx-font: 16 calibri;");
//		ShowQueryText.setTranslateX(SceneManager.scaledX(-695));
//		ShowQueryText.setTranslateY(SceneManager.scaledY(500));
		
		QueryBox = new HBox();
		QueryBox.getChildren().addAll(QueryText, QueryTextArea, InstructionText, QueryButton, ResetButton);
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

			String sql = "EXPLAIN (ANALYZE true, COSTS true, FORMAT json)" + QueryTextArea.getText() + ";";

			ResultSet rs = query.executeQuery(sql);

			// printing of region table
			System.out.println("postgresqlQEP");
			while (rs.next()) {
				String postgresqlQEP = rs.getString("QUERY PLAN");
				System.out.println(postgresqlQEP);
				Result = postgresqlQEP;
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
