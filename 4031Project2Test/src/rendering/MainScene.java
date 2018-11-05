package rendering;

import javafx.scene.input.MouseEvent;
import rendering.SceneManager.RenderableScene;
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
