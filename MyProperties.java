package util;

import java.io.IOException;
import java.util.Properties;

public class MyProperties {
	private static MyProperties myProperties;

	public static MyProperties getInstance() throws IOException {
		if (null == myProperties) {
			myProperties = new MyProperties();
		}
		return myProperties;
	}

	private static int open = 0;
	private static String value = "";
	private static Properties properties = new Properties();
	private static final String filePath = "/resource/Config.properties";

	public static int intReadConfig(String name) {
		try {
			// 可以用两种不同的流来加载配置文件
			// properties.load(new BufferedReader(new FileReader(filePath)));
			properties.load(MyProperties.class.getResourceAsStream(filePath));
			// 指定键名来获取值
			open = Integer.parseInt(properties.getProperty(name));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return open;
	}

	public static String StingReadConfig(String name) {
		try {
			// 可以用两种不同的流来加载配置文件
			// properties.load(new BufferedReader(new FileReader(filePath)));
			properties.load(MyProperties.class.getResourceAsStream(filePath));
			// 指定键名来获取值
			value = properties.getProperty(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}

}
