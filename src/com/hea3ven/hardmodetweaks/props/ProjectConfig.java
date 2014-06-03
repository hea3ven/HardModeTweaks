package com.hea3ven.hardmodetweaks.props;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProjectConfig {
	public static String version;
	public static String minecraft_version;
	public static String forge_version;
	
	static {
		InputStream propsStream = ProjectConfig.class.getClassLoader().getResourceAsStream("project.properties");
		if(propsStream != null)
		{
			Properties props = new Properties();
			try {
				props.load(propsStream);
				version = props.getProperty("version");
				if(version.equals("${version}"))
					version = "1.0";
				minecraft_version = props.getProperty("minecraft_version");
				if(minecraft_version.equals("${mcversion}"))
					minecraft_version = null;
				forge_version = props.getProperty("forge_version");
				if(forge_version.equals("${forgeversion}"))
					forge_version = null;
				else
					forge_version = forge_version.replaceFirst(minecraft_version + "-", "");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
