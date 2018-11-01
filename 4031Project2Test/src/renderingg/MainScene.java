package renderingg;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
//import config.AppVar;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import rendering.SceneManager.RenderableScene;
import rendering.SceneManager.SceneManager;

public class MainScene extends RenderableScene{
	
	private Button[] Menu;
	private Button[] Menu2;
	private TextArea something, something2;
	
	@Override
	public void setupNode()
	{
		
		// Rendering constants
		double buttonScaleX = Double.parseDouble(AppVar.getVar("buttonScaleX"));
		double buttonScaleY = Double.parseDouble(AppVar.getVar("buttonScaleY"));
		
		double menuOffsetX = Double.parseDouble(AppVar.getVar("menuOffsetX"));
		double menuOffsetY = Double.parseDouble(AppVar.getVar("menuOffsetY"));
				
		double buttonSpacing = Double.parseDouble(AppVar.getVar("buttonSpacing"));

		Menu = new Button[5];
		
		Menu[0] = new Button("Map Editor");
		Menu[0].setScaleX(buttonScaleX);
		Menu[0].setScaleY(buttonScaleY);
		Menu[0].setOnAction(actionEvent ->  {
		    this.getSm().changeScene("Edit"); 
		});
		
		Menu[1] = new Button("Exploration");
		Menu[1].setScaleX(buttonScaleX);
		Menu[1].setScaleY(buttonScaleY);
		Menu[1].setOnAction(actionEvent -> {
			this.getSm().changeScene("Exploration");
		});
		
		
		Menu[2] = new Button("Fastest Path");
		Menu[2].setScaleX(buttonScaleX);
		Menu[2].setScaleY(buttonScaleY);
		Menu[2].setOnAction(actionEvent -> {
			this.getSm().changeScene("Fastest Path");
		});
		
		Menu[3] = new Button("connect");
		Menu[3].setScaleX(buttonScaleX);
		Menu[3].setScaleY(buttonScaleY);
		Menu[3].setOnAction(actionEvent -> {
			comms.connect();
		});
		
		Menu[4] = new Button("disconnect");
		Menu[4].setScaleX(buttonScaleX);
		Menu[4].setScaleY(buttonScaleY);
		Menu[4].setOnAction(actionEvent -> {
			comms.disconnect();;
		});
		
		something = new TextArea();
		
		something.setId("GenText");
		something.setWrapText(true);
		something.setPrefRowCount(6);
		something.setTranslateY(SceneManager.scaledY(500));
		
		root.getChildren().add(something);
		
		something2 = new TextArea();
		
		something2.setId("GenText");
		something2.setWrapText(true);
		something2.setPrefRowCount(20);
		something2.setDisable(true);
		
		something2.setTranslateX(SceneManager.renderingResX - 200);
		
		root.getChildren().add(something2);
		
		Button [] Menu2 = new Button [2];
		
		Menu2[0] = new Button("Send Message");
		Menu2[0].setScaleX(buttonScaleX);
		Menu2[0].setScaleY(buttonScaleY);
		Menu2[0].setOnAction(actionEvent ->  {
		    comms.sendMessage(something.getText());
		});
		
		Menu2[1] = new Button("Clear received Messages");
		Menu2[1].setScaleX(buttonScaleX);
		Menu2[1].setScaleY(buttonScaleY);
		Menu2[1].setOnAction(actionEvent -> {
			comms.clearMsgList();
		});
		
		HBox menubox2 = new HBox();
		
		for (int i = 0; i < 2; i++){
			menubox2.getChildren().add(Menu2[i]);
			if (i == 0) continue;
			Menu2[i].setTranslateX(SceneManager.scaledX(i*buttonSpacing));
			
		}
		
		HBox menubox = new HBox();
		
		for (int i = 0; i < 5; i++){
			menubox.getChildren().add(Menu[i]);
			if (i == 0) continue;
			Menu[i].setTranslateX(SceneManager.scaledX(i*buttonSpacing));
			
		}
		menubox2.setTranslateX(SceneManager.scaledX(-300));
		menubox2.setTranslateY(SceneManager.scaledY(-100));
		menubox.getChildren().add(menubox2);
		
		menubox.setTranslateX(SceneManager.scaledX(menuOffsetX));
		menubox.setTranslateY(SceneManager.scaledY(SceneManager.renderingResY - menuOffsetY));
		
		root.getChildren().add(menubox);
		
	}
	
	// Per frame update
	@Override
	public void update(double deltaTime) {
		// TODO Auto-generated method stub
		String msgListString = "";
		
		for (int i = 0; i < comms.msgList.size(); i++)
			msgListString += comms.msgList.get(i) + "\n";
		something2.setText(msgListString);
		
		if (comms.isConnected())
		{
			String received = comms.getEarliestMsg(CommunicationManager.MessageType.BEGIN_EXPLORE);
			
			if (received != null)
			{
				comms.clearEarliestMsg(CommunicationManager.MessageType.BEGIN_EXPLORE);
				
				comms.beginexplore = true;
				
				this.getSm().changeScene("Exploration");
			}
			
		}
		
		
	}

	@Override
	public void mouseUpdate(MouseEvent event) {
		
		
	}



}
