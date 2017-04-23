package cache;

import java.util.HashMap;
import java.util.Map;

public class ResultCodeStorage {
	public static final String code_success = "00000";
	public static final String code_err_missing_parameter = "10001";
	public static final String code_err_invalid_parameter = "10002";
	public static final String code_err_invalid_login = "10003";
	public static final String code_err_invalid_access = "10004";
	public static final String code_err_unknown_server_internal_exception = "10500";
	private static Map<String, String> resultCodeStorage = new HashMap<String, String>();
	static {
		resultCodeStorage.put(code_success, "正常");
		resultCodeStorage.put(code_err_missing_parameter, "参数缺失");
		resultCodeStorage.put(code_err_invalid_login, "无效的登录状态");
		resultCodeStorage.put(code_err_invalid_parameter, "无效的参数");
		resultCodeStorage.put(code_err_invalid_access, "无效的权限");
		resultCodeStorage.put(code_err_unknown_server_internal_exception, "未知的服务器内部错误");
	}
	
	public static String getResultCodeDescription(String code) {
		return resultCodeStorage.get(code);
	}
}
