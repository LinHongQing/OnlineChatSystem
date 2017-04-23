package bean;

public class SystemConfigurationInfo {
	private static String mainSystemProtocol = "http";
	private static String mainSystemURL = "localhost";
	private static String mainSystemPort = "8080";
	private static String mainSystemName = "SchoolHelpingPlatform";
	public static String getMainSystemProtocol() {
		return mainSystemProtocol;
	}
	public static void setMainSystemProtocol(String mainSystemProtocol) {
		SystemConfigurationInfo.mainSystemProtocol = mainSystemProtocol;
	}
	public static String getMainSystemURL() {
		return mainSystemURL;
	}
	public static void setMainSystemURL(String mainSystemURL) {
		SystemConfigurationInfo.mainSystemURL = mainSystemURL;
	}
	public static String getMainSystemPort() {
		return mainSystemPort;
	}
	public static void setMainSystemPort(String mainSystemPort) {
		SystemConfigurationInfo.mainSystemPort = mainSystemPort;
	}
	public static String getMainSystemBasePath() {
		return mainSystemProtocol + "://" + mainSystemURL + ":" + mainSystemPort + "/" + mainSystemName + "/api/";
	}
}
