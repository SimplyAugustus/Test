package rendering.SceneManager;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.scene.input.MouseEvent;

public abstract class RenderableScene {
	// Private ref to scenemanager for changing scenes
	private SceneManager sm;
		
	// Root node of this scene
	public Group root;
		
	// The scene object that holds all the objects of this scene
	private Scene sc;
		
	// Name of this scene
	public String name = "";

	public boolean init(SceneManager sm, double Width, double Height, String name, CommunicationManager comms){
		
		// Init the Scene and group node
		root = new Group();
		
		sc = new Scene(root,Width, Height, Color.WHITE);
				
		// Store reference of scenemanager
		this.setSm(sm);
				
		// Setup the scene graph
		setupNode();
		
		// Name identifier
		this.name = name;
		
		sc.addEventHandler(MouseEvent.ANY, new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent event)
			{
				mouseUpdate(event);
			}
			
		});
		
		return true;
	}
	
	// Setup the group node
	public abstract void setupNode();
	
	// Frame by Frame update
	public abstract void update(double deltaTime);
	
	// Mouse updates
	public abstract void mouseUpdate(MouseEvent event);

	public SceneManager getSm() {
		return sm;
	}

	public void setSm(SceneManager sm) {
		this.sm = sm;
	}

	public Scene getSc() {
		return sc;
	}

	public void setSc(Scene sc) {
		this.sc = sc;
	}
}
