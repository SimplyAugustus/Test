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
	private HBox NodeBox;
	private VBox DiagramBox;
	private Rectangle BoxRectangle;
	private Group QueryArea, QueryTextGroup, InstructionTextGroup, QueryTextAreaGroup,
	QueryButtonGroup, ResetButtonGroup, LineGroup, StackGroup, NodeGroup;
	private Button QueryButton, ResetButton;
	private ScrollPane scrollPane;
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
		
		scrollPane = new ScrollPane();
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
		
		QueryTextGroup = new Group();
		QueryTextGroup.getChildren().add(QueryText);
		QueryTextGroup.setTranslateX(SceneManager.scaledX(-132));
		QueryTextGroup.setTranslateY(SceneManager.scaledY(0));
		
		ResetButton = new Button("Reset");
		ResetButton.setScaleX(SceneManager.scaledX(2));
		ResetButton.setScaleY(SceneManager.scaledY(2));
		ResetButton.setOnAction(actionEvent -> {
			root.getChildren().clear();
			init(root);
		});
		
		ResetButtonGroup = new Group();
		ResetButtonGroup.getChildren().add(ResetButton);
		ResetButtonGroup.setTranslateX(SceneManager.scaledX(5));
		ResetButtonGroup.setTranslateY(SceneManager.scaledY(100));
		
		QueryButton = new Button("Submit");
		QueryButton.setScaleX(SceneManager.scaledX(2));
		QueryButton.setScaleY(SceneManager.scaledY(2));
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
				
				StackGroup = new Group();
				StackGroup.getChildren().addAll(stack);
				StackGroup.setTranslateX(SceneManager.scaledX(0));
				StackGroup.setTranslateY(SceneManager.scaledY(50));
				
				NodeGroup = new Group();
				NodeGroup.setTranslateX(SceneManager.scaledX(460));
				NodeGroup.setTranslateY(SceneManager.scaledY(0));

				if (j != (i - 2)) {
					Line = new Line();
					Line.setStartX(0.0f);
					Line.setStartY(0.0f);
					Line.setEndX(0.0f);
					Line.setEndY(50.0f);

					LineGroup = new Group();
					LineGroup.getChildren().addAll(Line);
					LineGroup.setTranslateX(SceneManager.scaledX(68));
					LineGroup.setTranslateY(SceneManager.scaledY(113));
					
					NodeGroup.getChildren().addAll(StackGroup, LineGroup);
					
				} else {
					NodeGroup.getChildren().add(StackGroup);
				}
				
				DiagramBox.getChildren().add(NodeGroup);
				j++;
			}

			root.getChildren().add(scrollPane);
			
		});
		
		QueryButtonGroup = new Group();
		QueryButtonGroup.getChildren().addAll(QueryButton, ResetButtonGroup);
		QueryButtonGroup.setTranslateX(SceneManager.scaledX(530));
		QueryButtonGroup.setTranslateY(SceneManager.scaledY(150));
		
		InstructionText = new Text(instructionText);
		InstructionText.setId("InstructionText");
		InstructionText.setStyle("-fx-font: 16 calibri;");
		
		InstructionTextGroup = new Group();
		InstructionTextGroup.getChildren().add(InstructionText);
		InstructionTextGroup.setTranslateX(SceneManager.scaledX(-130));
		InstructionTextGroup.setTranslateY(SceneManager.scaledY(500));
		
		QueryTextArea = new TextArea();
		QueryTextArea.setId("QueryText");
		QueryTextArea.setWrapText(true);
		QueryTextArea.setPrefColumnCount(35);
		QueryTextArea.setPrefRowCount(15);
		QueryTextArea.setTranslateX(SceneManager.scaledX(-130));
		QueryTextArea.setTranslateY(SceneManager.scaledY(50));
		
		QueryTextAreaGroup = new Group();
		QueryTextAreaGroup.getChildren().addAll(QueryTextArea, QueryTextGroup, InstructionTextGroup, QueryButtonGroup);
		QueryTextAreaGroup.setTranslateX(SceneManager.scaledX(0));
		QueryTextAreaGroup.setTranslateY(SceneManager.scaledY(0));
		
		
//		ShowQueryText = new Text();
//		ShowQueryText.setId("ShowQueryText");
//		ShowQueryText.setStyle("-fx-font: 16 calibri;");
//		ShowQueryText.setTranslateX(SceneManager.scaledX(-695));
//		ShowQueryText.setTranslateY(SceneManager.scaledY(500));
		

		QueryArea = new Group();
		QueryArea.getChildren().add(QueryTextAreaGroup);
		QueryArea.setTranslateX(SceneManager.scaledX(1200));
		QueryArea.setTranslateY(SceneManager.scaledY(100));

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
