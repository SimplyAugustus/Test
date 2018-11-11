package config;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * Handles the loading of variables from AppProp
 *
 */
public class AppVar {
	// App properties (for loading config)
	private static Properties AppProp = null;
	
	// This should never be changed unless the path of app.properties is changed
	public static final String configFilePath = "app.properties";
	
	/**
	 * Simple function to load configuration file
	 * @param filename File name of config
	 */
	public static void loadConfig()
	{
		// Init properties if null
		if (AppProp == null)
		{
			AppProp = new Properties();
		}
		
		try {
			FileInputStream input = new FileInputStream(configFilePath);
			
			if (input != null){
				AppProp.load(input);
				input.close();
				System.out.println("Config file loaded!");
			}
			
		} catch (Exception e) {
			System.out.println("Error loading config! Make sure it is inside the root directory!");
		}			
	}
	
	public static String getVar(String varname)
	{
		return AppProp.getProperty(varname);
	}
}
