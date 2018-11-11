package rendering.SceneManager;

import java.util.*;

import application.Main;
import config.AppVar;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rendering.MainScene;


public class SceneManager {
	
	private List<RenderableScene> SceneList;
	
	public static double renderingResX;
	public static double renderingResY;
	public static double primaryResX;
	public static double primaryResY;
	
	public RenderableScene curScene;
	
	// Reference to primary stage
	private static Stage primaryStage;

	private MainScene mainScene;
	
	
	public boolean init (double Width, double Height, Stage primaryStage, String cssfile)
	{
		renderingResX = Double.parseDouble(AppVar.getVar("renderingResX"));
		renderingResY = Double.parseDouble(AppVar.getVar("renderingResY"));
		
		primaryResX = Double.parseDouble(AppVar.getVar("primaryResX"));
		primaryResY = Double.parseDouble(AppVar.getVar("primaryResY"));
		
		SceneList = new ArrayList<RenderableScene>();
			
		SceneManager.primaryStage = primaryStage;
		
		mainScene = new MainScene();
		mainScene.init(this, Width, Height, "Main");
		
		addScene(mainScene, cssfile);
		changeScene("Main");
		return true;
	}
	
	// Add a Scene to the Scene Manager
	private void addScene(RenderableScene newScene, String css){
		
		// Add css file
		newScene.getSc().getStylesheets().add(css);
		
		SceneList.add(newScene);
	}
	
	// Change the current Scene
	public boolean changeScene(String name){
		// Loop through list
		for (int i = 0; i < SceneList.size(); i++){
			// Match by name
			if (SceneList.get(i).name.equals(name)){
				
				// Set cur Scene
				curScene = SceneList.get(i);
				primaryStage.setScene(curScene.getSc());
				
				return true;
			}
		}
		
		return false;
	}
	
	// For scaling so that rendering will look the same on different coms
	public static double scaledX(double X)
	{	
		return X / renderingResX * primaryResX;
	}
	
	public static double scaledY(double Y)
	{
		return Y / renderingResY * primaryResY;
	}
}
