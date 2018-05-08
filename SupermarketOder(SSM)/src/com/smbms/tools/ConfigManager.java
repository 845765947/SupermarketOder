package com.smbms.tools;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.smbms.tools.ConfigManager;

public class ConfigManager {

	private static ConfigManager configManager;
	private static Properties properties;

	private ConfigManager() {
		String configFile = "database.properties";
		properties = new Properties();
		InputStream is = ConfigManager.class.getClassLoader()
				.getResourceAsStream(configFile);
		try {
			properties.load(is);
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static class ConfigManagerHepler {
		private static final ConfigManager INSTANCE = new ConfigManager();
	}

	public static ConfigManager getInstance() {
		configManager = ConfigManagerHepler.INSTANCE;
		return configManager;
	}

	public String getValue(String key) {
		String result = properties.getProperty(key);
		return result;
	}
}
