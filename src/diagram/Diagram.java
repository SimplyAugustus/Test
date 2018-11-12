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
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.scene.paint.Color;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import rendering.SceneManager.SceneManager;
import diagram.Diagram;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import config.AppVar;
import org.json.JSONArray;

public class Diagram {

	private Text QueryText, EnteredQueryText, BoxText, InstructionText;
	private TextArea QueryTextArea;
	private HBox NodeBox;
	private VBox DiagramBox;
	private Rectangle BoxRectangle;
	private Group QueryArea, QueryTextGroup, InstructionTextGroup, QueryTextAreaGroup,
	QueryButtonGroup, ResetButtonGroup, LineGroup, StackGroup, NodeGroup, HighlightedQueryGroup, EnteredQueryTextGroup;
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
	private boolean reset = false;
	private TextFlow textFlowPlane = new TextFlow();
	private boolean afterBracket = false;
	private int counter = 0;
	private int queueSize = 0;
	private Queue<String> queue = new LinkedList<>();
	private JSONArray array;
	
	public void init(Group root) {
		
		instructionText = AppVar.getVar("instruction");
		
		DiagramBox = new VBox();
		
		
		
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
			reset = true;
			connect();
			root.getChildren().clear();
			textFlowPlane.getChildren().clear();
			init(root);
			QueryButton.setDisable(false);
		});
		
		ResetButtonGroup = new Group();
		ResetButtonGroup.getChildren().add(ResetButton);
		ResetButtonGroup.setTranslateX(SceneManager.scaledX(5));
		ResetButtonGroup.setTranslateY(SceneManager.scaledY(100));
		
		QueryButton = new Button("Submit");
		QueryButton.setScaleX(SceneManager.scaledX(2));
		QueryButton.setScaleY(SceneManager.scaledY(2));
		QueryButton.setOnAction(actionEvent -> {
			
			QueryButton.setDisable(true);
			
			scrollPane = new ScrollPane();
			scrollPane.setContent(DiagramBox);
			scrollPane.setFitToWidth(true);
			scrollPane.setTranslateX(SceneManager.scaledX(20));
			scrollPane.setPrefViewportWidth(780);
			scrollPane.setPrefViewportHeight(800);
			scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
			scrollPane.setPannable(true); 
			
			connect();
			int j = 1;
			
			try {
				array = new JSONArray(Result);
				 for(int k=0; k < array.length(); k++){
			            System.out.println(array.get(k));
			            String test = array.get(k).toString();
			            
			            String testArray[] = test.split("\"Plans\":", 2);
			            String test1 = new String(testArray[0]);
			            System.out.println("test1:" + test1);
			            
			            String testArray1[] = test1.split("\"Plan\":", 2);
			            String test2 = new String(testArray1[1]);
			            System.out.println("test2 (StringA):" + test2);
			            String test3 = new String(testArray[1]);
			            System.out.println("test3 (Plan + StringB):" + test3);
			            String test4 = new String(test3.substring(0, test3.lastIndexOf("]")));
			            splitPlans(test4);
			            System.out.println("test4 (Plan):" + test4);
			            String test5 = new String(test3.substring(test3.lastIndexOf("]")+2));
			            System.out.println("test5 (StringB):" + test5);
			            String test6 = new String(test2 + test5);
			            System.out.println("test6 (StringA + StringB):" + test6);
			            
			            String testArray2[] = test6.split(",\"Execution Time", 2);
			            String test7 = new String(testArray2[0]);
			            test7 = test7.replaceAll("[\\[\\](){}]", "");
						test7 = test7.replaceAll("\"", "");
						test7 = test7.replaceAll(",", "\n");
			            System.out.println("test7 (Refined StringA + StringB):" + test7);
			            queue.add(test7);
			            System.out.println("queue:\n" + queue);
			            queueSize = queue.size();
			            
			        }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			while (!queue.isEmpty()) {
				String steps = new String();
				steps = queue.remove();
				
				String stepArray1[] = steps.split("Node Type:", 2);
				String stepText = new String(stepArray1[1] + stepArray1[0]);
				
				String stepArray2[] = stepText.split("\n", 2);
				String stepNode = new String(stepArray2[0]);
				String stepDetail = new String(stepArray2[1]);

				if (stepNode.isEmpty()) {
					break;
				}
				System.out.println("StepDetail " + j + ": " + stepDetail);

				System.out.println("StepNode " + j + ":" + stepNode);

				StackPane stack = new StackPane();

				BoxRectangle = new Rectangle(0, 0, 100, 50);
				BoxRectangle.setFill(Color.TRANSPARENT);
				BoxRectangle.setStroke(Color.BLACK);

				BoxText = new Text(stepNode);
				
				switch(stepNode) {
					case "Sort":
						BoxText.setFill(Color.BLUE);
						System.out.println("Setting Blue");
						break;
					case "Aggregate":
						BoxText.setFill(Color.GREEN);
						System.out.println("Setting Green");
						break;
					case "Seq Scan":
						BoxText.setFill(Color.ORANGE);
						System.out.println("Setting Orange");
						break;
					case "Index Only Scan":
						BoxText.setFill(Color.PURPLE);
						System.out.println("Setting Purple");
						break;
					case "Index Scan":
						BoxText.setFill(Color.PURPLE);
						System.out.println("Setting Purple");
						break;						
				}
				
				Tooltip.install(stack, new Tooltip(stepDetail));
				stack.getChildren().addAll(BoxRectangle, BoxText);
				
				StackGroup = new Group();
				StackGroup.setTranslateX(SceneManager.scaledX(0));
				StackGroup.setTranslateY(SceneManager.scaledY(50));
				
				NodeGroup = new Group();
				NodeGroup.setTranslateX(SceneManager.scaledX(200));
				NodeGroup.setTranslateY(SceneManager.scaledY(0));
				
				if (j != queueSize) {
					Line = new Line();
					Line.setStartX(0.0f);
					Line.setStartY(0.0f);
					Line.setEndX(0.0f);
					Line.setEndY(50.0f);
					Line. setTranslateX(SceneManager.scaledX(77));
					
					LineGroup = new Group();
					LineGroup.getChildren().addAll(Line);
//					LineGroup.setTranslateX(SceneManager.scaledX(0));
					LineGroup.setTranslateY(SceneManager.scaledY(80));
					
					StackGroup.getChildren().addAll(stack, LineGroup);

				} else {
					StackGroup.getChildren().add(stack);
				}
				
				NodeGroup.getChildren().add(StackGroup);
				DiagramBox.getChildren().add(NodeGroup);
				j++;
			}
			
			root.getChildren().add(scrollPane);			
		});
		
		QueryButtonGroup = new Group();
		QueryButtonGroup.getChildren().addAll(QueryButton, ResetButtonGroup);
		QueryButtonGroup.setTranslateX(SceneManager.scaledX(590));
		QueryButtonGroup.setTranslateY(SceneManager.scaledY(150));
		
		InstructionText = new Text(instructionText);
		InstructionText.setId("InstructionText");
		InstructionText.setStyle("-fx-font: 16 calibri;");
		
		InstructionTextGroup = new Group();
		InstructionTextGroup.getChildren().add(InstructionText);
		InstructionTextGroup.setTranslateX(SceneManager.scaledX(0));
		InstructionTextGroup.setTranslateY(SceneManager.scaledY(350));
		
		QueryTextArea = new TextArea();
		QueryTextArea.setId("QueryText");
		QueryTextArea.setWrapText(true);
		QueryTextArea.setPrefColumnCount(35);
		QueryTextArea.setPrefRowCount(15);
		QueryTextArea.setTranslateX(SceneManager.scaledX(-130));
		QueryTextArea.setTranslateY(SceneManager.scaledY(50));
		
		HighlightedQueryGroup = new Group();
		HighlightedQueryGroup.getChildren().addAll(textFlowPlane, InstructionTextGroup);
		HighlightedQueryGroup.setTranslateX(SceneManager.scaledX(0));
		HighlightedQueryGroup.setTranslateY(SceneManager.scaledY(10));

		EnteredQueryText = new Text("Submitted Query: ");
		EnteredQueryText.setId("EnteredQueryText");
		EnteredQueryText.setStyle("-fx-font: 16 calibri;");
		
		EnteredQueryTextGroup = new Group();
		EnteredQueryTextGroup.getChildren().addAll(EnteredQueryText, HighlightedQueryGroup);
		EnteredQueryTextGroup.setTranslateX(SceneManager.scaledX(-130));
		EnteredQueryTextGroup.setTranslateY(SceneManager.scaledY(500));
		
		QueryTextAreaGroup = new Group();
		QueryTextAreaGroup.getChildren().addAll(QueryTextArea, QueryTextGroup, EnteredQueryTextGroup,
				QueryButtonGroup);
		QueryTextAreaGroup.setTranslateX(SceneManager.scaledX(0));
		QueryTextAreaGroup.setTranslateY(SceneManager.scaledY(0));
	
		QueryArea = new Group();
		QueryArea.getChildren().add(QueryTextAreaGroup);
		QueryArea.setTranslateX(SceneManager.scaledX(800));
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
			String enteredQuery = new String();
			enteredQuery = QueryTextArea.getText();
			System.out.println("Entered query: " + enteredQuery);
			splitSelect(enteredQuery);
			ResultSet rs = query.executeQuery(sql);

			// printing of region table
			System.out.println("postgresqlQEP");
			while (rs.next()) {
				String postgresqlQEP = rs.getString("QUERY PLAN");
				System.out.println(postgresqlQEP);
				if(reset){
					Result = "";
					reset = false;
				}
				else {
				Result = postgresqlQEP;
				}
			}

			rs.close();
			query.close();
			conn.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return conn;
	}
	
	public void splitSelect(String textArea) {
		
		String text = textArea;
		if(text.isEmpty() ){
			return;
		}
			
		textFlowPlane.setTextAlignment(TextAlignment.JUSTIFY);
		textFlowPlane.setPrefSize(500,300);
		textFlowPlane.setLineSpacing(5.0);
		
		String queryArray1[] = textArea.split("SELECT", 2);
		String splitQuery1 = new String(queryArray1[1]);
		System.out.println("Split Query1: " + splitQuery1);
		Text queryText1 = new Text("SELECT");
		textFlowPlane.getChildren().add(queryText1);
		
		String queryArray2[] = splitQuery1.split("FROM", 2);
		String splitQuery2 = new String(queryArray2[0]);
		System.out.println("Split Query2 (SELECT) : " + splitQuery2);
		Text queryText2 = new Text(splitQuery2);
		queryText2.setFill(Color.BLUE);
		textFlowPlane.getChildren().add(queryText2);
		
		String splitQuery3 = new String(queryArray2[1]);
		System.out.println("Split Query3: " + splitQuery3);
		splitWhere(splitQuery3);
		
	}
	
	public void splitWhere(String textArea) {
		
		String queryArray3[] = textArea.split("WHERE", 2);
		
		if(afterBracket){
			Text queryText3 = new Text(queryArray3[0] + "WHERE");
			textFlowPlane.getChildren().add(queryText3);
			afterBracket = false;
		}
		else {
			Text queryText3 = new Text("FROM" + queryArray3[0] + "WHERE");
			textFlowPlane.getChildren().add(queryText3);

		}
		
		String splitQuery4 = new String(queryArray3[1]);
		System.out.println("Split Query4: " + splitQuery4);
		
		if(splitQuery4.contains(") ") && splitQuery4.contains("GROUP BY")){
			if(splitQuery4.indexOf(")") < splitQuery4.indexOf("GROUP BY") ) {
				String queryArray4[] = splitQuery4.split("\\) ", 2);
				String splitQuery5 = new String(queryArray4[0]);
				System.out.println("Split Query5 (WHERE): " + splitQuery5);
				
				checkCondition(splitQuery5);
				
				Text queryText5 = new Text(") ");
				textFlowPlane.getChildren().add(queryText5);
				
				String splitQuery6 = new String(queryArray4[1]);
				System.out.println("Split Query6: " + splitQuery6);
				afterBracket = true;
				splitWhere(splitQuery6);
				
			}
			else{
				System.out.println("else is working");
				String queryArray4[] = splitQuery4.split("GROUP BY", 2);
				String splitQuery5 = new String(queryArray4[0]);
				System.out.println("Split Query5 (WHERE): " + splitQuery5);
				
				checkCondition(splitQuery5);
	
				String splitQuery6 = new String(queryArray4[1]);
				
				if(splitQuery6.contains("WHERE")){
					String queryArray5[] = splitQuery6.split("\\)", 2);
					String splitQuery7 = new String(queryArray5[0]);
					System.out.println("Split Query7 (GROUP BY): " + splitQuery7);
					
					Text queryText5 = new Text("GROUP BY");
					textFlowPlane.getChildren().add(queryText5);
					
					Text queryText6 = new Text(splitQuery7);
					queryText6.setFill(Color.GREEN);
					textFlowPlane.getChildren().add(queryText6);
					
					Text queryText7 = new Text(")");
					textFlowPlane.getChildren().add(queryText7);
					
					String splitQuery8 = new String(queryArray5[1]);
					afterBracket = true;
					splitWhere(splitQuery8);
				}
				else {
					System.out.println("Split Query6 (GROUP BY): " + splitQuery6);
					
//					Text queryText5 = new Text("GROUP BY");
//					textFlowPlane.getChildren().add(queryText5);
//
//					Text queryText6 = new Text(splitQuery6);
//					queryText6.setFill(Color.GREEN);
//					textFlowPlane.getChildren().add(queryText6);
					checkOrder(splitQuery6);
					
					return;
				}
			}
		}
		else if(splitQuery4.contains(") ") && !splitQuery4.contains("GROUP BY")){
			String queryArray4[] = splitQuery4.split("\\) ", 2);
			String splitQuery5 = new String(queryArray4[0]);
			System.out.println("Split Query5 (WHERE): " + splitQuery5);
			
			checkCondition(splitQuery5);
		
			Text queryText5 = new Text(") ");
			textFlowPlane.getChildren().add(queryText5);
			
			String splitQuery6 = new String(queryArray4[1]);
			System.out.println("Split Query6: " + splitQuery6);
			afterBracket = true;
			splitWhere(splitQuery6);
		}
		else if (!splitQuery4.contains(") ") && splitQuery4.contains("GROUP BY")){
			System.out.println("else2 is working");
			String queryArray4[] = splitQuery4.split("GROUP BY", 2);
			String splitQuery5 = new String(queryArray4[0]);
			System.out.println("Split Query5 (WHERE): " + splitQuery5);
			
			checkCondition(splitQuery5);
			
			String splitQuery6 = new String(queryArray4[1]);
			 
			if(splitQuery6.contains("WHERE")){
				String queryArray5[] = splitQuery6.split("\\)", 2);
				String splitQuery7 = new String(queryArray5[0]);
				System.out.println("Split Query7 (GROUP BY): " + splitQuery7);
				
				Text queryText5 = new Text("GROUP BY");
				textFlowPlane.getChildren().add(queryText5);
				
				Text queryText6 = new Text(splitQuery7);
				queryText6.setFill(Color.GREEN);
				textFlowPlane.getChildren().add(queryText6);
				
				Text queryText7 = new Text(")");
				textFlowPlane.getChildren().add(queryText7);
				
				
				String splitQuery8 = new String(queryArray5[1]);
				splitWhere(splitQuery8);
			}
			else {
				System.out.println("Split Query6 (GROUP BY): " + splitQuery6);
				
//				Text queryText5 = new Text("GROUP BY");
//				textFlowPlane.getChildren().add(queryText5);
//				
//				Text queryText6 = new Text(splitQuery6);
//				queryText6.setFill(Color.GREEN);
//				textFlowPlane.getChildren().add(queryText6);
				checkOrder(splitQuery6);
				
				return;
			}
		}
		else if (!splitQuery4.contains(") ") && !splitQuery4.contains("GROUP BY")){
			System.out.println("Split Query4 (WHERE): " + splitQuery4);
			
			checkCondition(splitQuery4);
			
			return;
		}
		
	}
	
	public void checkCondition (String queryText) {
		String splitQuery5 = new String(queryText);
		
		if (splitQuery5.contains("'")) {
			if (splitQuery5.contains("AND")) {
				String andArray1[] = splitQuery5.split("AND", 2);
				String andString1 = new String(andArray1[0]);
				String andString2 = new String(andArray1[1]);
				
				if(!andString1.contains("'")){
					Text queryText4 = new Text(splitQuery5);
					queryText4.setFill(Color.PURPLE);
					textFlowPlane.getChildren().add(queryText4);
				}
				else {
					if(andString2.contains("AND")){
						String andArray2[] = splitQuery5.split("AND", 2);
						String andString11 = new String(andArray2[0]);
						String andString22 = new String(andArray2[1]);
						
						if(!andString11.contains("'")){
							Text queryText4 = new Text(splitQuery5);
							queryText4.setFill(Color.PURPLE);
							textFlowPlane.getChildren().add(queryText4);
						}
						else {
							if(!andString22.contains("'")){
								Text queryText4 = new Text(splitQuery5);
								queryText4.setFill(Color.PURPLE);
								textFlowPlane.getChildren().add(queryText4);
							}
							else {
								Text queryText4 = new Text(splitQuery5);
								queryText4.setFill(Color.ORANGE);
								textFlowPlane.getChildren().add(queryText4);
							}
						}
					}
					else {
						if(andString2.contains("'")){
							Text queryText4 = new Text(splitQuery5);
							queryText4.setFill(Color.ORANGE);
							textFlowPlane.getChildren().add(queryText4);
						}
						else {
							Text queryText4 = new Text(splitQuery5);
							queryText4.setFill(Color.PURPLE);
							textFlowPlane.getChildren().add(queryText4);
						}
					}
				}
				
			}
			else {
				Text queryText4 = new Text(splitQuery5);
				queryText4.setFill(Color.ORANGE);
				textFlowPlane.getChildren().add(queryText4);
			}
			
		} 
		else {
			if(splitQuery5.contains("<")||splitQuery5.contains(">")){
				Text queryText4 = new Text(splitQuery5);
				queryText4.setFill(Color.ORANGE);
				textFlowPlane.getChildren().add(queryText4);
			}
			else if(splitQuery5.contains("BETWEEN")){
				Text queryText4 = new Text(splitQuery5);
				queryText4.setFill(Color.ORANGE);
				textFlowPlane.getChildren().add(queryText4);
			}
			else {
				Text queryText4 = new Text(splitQuery5);
				queryText4.setFill(Color.PURPLE);
				textFlowPlane.getChildren().add(queryText4);
			}
		}
	}
	
	public void checkOrder(String queryText) {
		String splitQuery6 = new String(queryText);
		
		if(splitQuery6.contains("ORDER BY")){
			String orderArray[] = splitQuery6.split("ORDER BY", 2);
			String orderString1 = new String(orderArray[0]);
			String orderString2 = new String(orderArray[1]);
			
			Text queryText5 = new Text("GROUP BY");
			textFlowPlane.getChildren().add(queryText5);

			Text queryText6 = new Text(orderString1);
			queryText6.setFill(Color.GREEN);
			textFlowPlane.getChildren().add(queryText6);
			
			Text queryText7 = new Text("ORDER BY");
			textFlowPlane.getChildren().add(queryText7);
			
			Text queryText8 = new Text(orderString2);
			textFlowPlane.getChildren().add(queryText8);
		}
		else {
			Text queryText5 = new Text("GROUP BY");
			textFlowPlane.getChildren().add(queryText5);

			Text queryText6 = new Text(splitQuery6);
			queryText6.setFill(Color.GREEN);
			textFlowPlane.getChildren().add(queryText6);
		}
	}
	
	public void splitPlans(String planString) {
		 counter++;
		 String test = new String(planString);
		
		 String testArray[] = test.split("\"Plans\":", 2);
        String test1 = new String(testArray[0]);
        System.out.println(counter + " test1 (StringA):" + test1);
       
        
        if(testArray.length == 1 ){
       	 return;
        }
        
        String test3 = new String(testArray[1]);
        System.out.println(counter + " test3 (Plan + String B):" + test3);
        
        String test4 = new String(test3.substring(0, test3.lastIndexOf("]")));
        System.out.println(counter + " test4 (Plan):" + test4);
        
        if(test4.contains("\"Plans\":")){
       	splitPlans(test4);
        	counter--;
        }
        else {
       	if(test4.contains(",{\"Parent Relationship\"")){
       		System.out.print("test4 is this:" + test4);
       		String testArray2[] = test4.split(",\\{\"Parent Relationship\"", 2);
       		String test7 = new String(testArray2[0]);
       		test7 = test7.replaceAll("[\\[\\](){}]", "");
   			test7 = test7.replaceAll("\"", "");
   			test7 = test7.replaceAll(",", "\n");
       		System.out.println("test7 (Plan AA):" + test7);
       		queue.add(test7 + "\n");
       		String test8 = new String("{\"Parent Relationship\"" + testArray2[1]);
       		test8 = test8.replaceAll("[\\[\\](){}]", "");
   			test8 = test8.replaceAll("\"", "");
   			test8 = test8.replaceAll(",", "\n");
       		System.out.println("test8 (Plan BB):" + test8);
       		queue.add(test8 + "\n");
       	}
       	else {
       		test4 = test4.replaceAll("[\\[\\](){}]", "");
   			test4 = test4.replaceAll("\"", "");
   			test4 = test4.replaceAll(",", "\n");
       		queue.add(test4 + "\n");
       	}
        }
    	String test5 = new String(test3.substring(test3.lastIndexOf("]") + 2));
		System.out.println(counter + " test5 (StringB):" + test5);
		String test6 = new String(test1 + test5);
		System.out.println(counter + " test6 (StringA + StringB):" + test6);
		if(test6.contains(",{\"Parent Relationship\"")){
   		String testArray3[] = test6.split(",\\{\"Parent Relationship\"", 2);
   		String test9 = new String(testArray3[0]);
   		test9 = test9.replaceAll("[\\[\\](){}]", "");
			test9 = test9.replaceAll("\"", "");
			test9 = test9.replaceAll(",", "\n");
   		System.out.println("test9 (Plan AA):" + test9);
   		queue.add(test9 + "\n");
   		String test10 = new String("{\"Parent Relationship\"" + testArray3[1]);
   		test10 = test10.replaceAll("[\\[\\](){}]", "");
			test10 = test10.replaceAll("\"", "");
			test10 = test10.replaceAll(",", "\n");
   		System.out.println("test10 (Plan BB):" + test10);
   		queue.add(test10 + "\n");
   	}
		else {
			test6 = test6.replaceAll("[\\[\\](){}]", "");
			test6 = test6.replaceAll("\"", "");
			test6 = test6.replaceAll(",", "\n");
			queue.add(test6 + "\n");
		}
	}
}
