package application;
	
import java.io.*;
import java.net.URL;
import java.util.Objects;
import java.util.Properties;

import config.AppVar;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import rendering.SceneManager.SceneManager;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Main extends Application {
	
	// Mouse event handler
	public Rectangle2D primaryScreenBounds;
	
	public SceneManager sm;
		
	int frameCount;						// Current frame number
	double fps;							// Frames per second
	long currentTime, previousTime;		// Time of the current frame and previous frame
	
	long timeLastUpdate;				// Time of last update to enforce FPS
	
	double frequency;					// Frequency of frames (User defined FPS)
	double deltaTime;					// Time Between Frames

	
	@Override
	public void start(Stage primaryStage) {
		try {
			
			// Get user defined FPS
			frequency = Double.parseDouble(AppVar.getVar("FPS"));
			
			deltaTime = 0;

			timeLastUpdate = System.nanoTime();
			
			previousTime = 0;
			currentTime = 0;
			frameCount = 0;
			fps = 0;
						
			primaryStage.setTitle(AppVar.getVar("app.name"));
			
			Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

			//set Stage boundaries to visible bounds of the main screen
			primaryStage.setX(primaryScreenBounds.getMinX());
			primaryStage.setY(primaryScreenBounds.getMinY());
			primaryStage.setWidth(primaryScreenBounds.getWidth());
			primaryStage.setHeight(primaryScreenBounds.getHeight());
			
			primaryStage.setMaximized(true);
			
			String cssfile = getClass().getResource("application.css").toExternalForm();
			
			// Init scene manager and inform it of the primary stage
			sm = new SceneManager();
			
			sm.init(primaryScreenBounds.getWidth(), primaryScreenBounds.getHeight(), primaryStage, cssfile);

			
			primaryStage.setScene(sm.curScene.getSc());
			sm.curScene.getSc().getStylesheets().add(cssfile);
			
 			primaryStage.show();
 			
 			new AnimationTimer() {
	            @Override
	            public void handle(long now) {
	            	
	            	// Frame independant updates
					if ((now - timeLastUpdate)>1000000000.0 / frequency) 
					{
						// Calculate the framerate
						calculateFPS(now);
						// Get time between frames
						deltaTime = (now - timeLastUpdate)/1000000000.0;
						
						//System.out.println("timeLastUpdate " + timeLastUpdate);
						//System.out.println("now: " + now);
						// Set the tick of the last update
						timeLastUpdate = now;
						
						
						//System.out.println("Deltatime: " + deltaTime);
						
//						// Perform update
//						sm.curScene.update(deltaTime);
					}
	            }
	        }.start();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		// Load config
		AppVar.loadConfig();
		
		launch(args);
	}
	
	// Calculating The FPS
	private void calculateFPS(long tick) {
		// Increase frame count
		frameCount++;

		currentTime = tick;

		// Calculate time passed
		int timeInterval = (int)(currentTime - previousTime);

		// 1000000000 nanoseconds is one second
		if (timeInterval> 1000000000) {
			// Calculate the number of frames per second
			fps = frameCount / (timeInterval / 1000000000);

			// Set time
			previousTime = currentTime;

			// Reset frame count
			frameCount = 0;
		}
	}
}