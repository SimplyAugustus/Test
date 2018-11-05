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


public class MainScene extends RenderableScene{
   
	private Diagram Diagram;
	
	@Override
	public void setupNode()
	{
		
		Diagram = new Diagram();
		Diagram.init(root);
		
	}

	@Override
	public void mouseUpdate(MouseEvent event) {
		
		
	}

}
