package util;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;

public class JSONUtil {

	private JSONObject json = null;

	public static String generateJSONString(String[] args) {
		try {
			if (args != null) {
				JSONObject json = new JSONObject();
				for (int i = 0; i < args.length; i += 2) {
					json.put(args[i], args[i + 1]);
				}
				return json.toString();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}

	/**
	 * 格式化
	 * @param jsonStr
	 * @return
	 */
	public static String formatJSON(String jsonStr) {
		if (null == jsonStr || "".equals(jsonStr))
			return "";
		StringBuilder sb = new StringBuilder();
		char last = '\0';
		char current = '\0';
		int indent = 0;
		for (int i = 0; i < jsonStr.length(); i++) {
			last = current;
			current = jsonStr.charAt(i);
			switch (current) {
			case '{':
			case '[':
				sb.append(current);
				sb.append('\n');
				indent++;
				addIndentBlank(sb, indent);
				break;
			case '}':
			case ']':
				sb.append('\n');
				indent--;
				addIndentBlank(sb, indent);
				sb.append(current);
				break;
			case ',':
				sb.append(current);
				if (last != '\\') {
					sb.append('\n');
					addIndentBlank(sb, indent);
				}
				break;
			default:
				sb.append(current);
			}
		}
		return sb.toString();
	}

	/**
	 * 添加space
	 * @param sb
	 * @param indent
	 */
	private static void addIndentBlank(StringBuilder sb, int indent) {
		for (int i = 0; i < indent; i++) {
			sb.append('\t');
		}
	}

	public String resolveJSONString(String param) {
		if (json != null) {
			try {
				return json.get(param).toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public Object resolveJSON(String param) {
		if (json != null) {
			try {
				return json.get(param);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
	public String formatJSON() {
		if (json != null)
			return formatJSON(json.toString());
		else
			return "Empty JSON String!";
	}

	public JSONUtil(String jsonStr) {
		super();
		// TODO Auto-generated constructor stub
		try {
			json = (JSONObject) new JSONTokener(jsonStr).nextValue();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public JSONObject getJSONObject() {
		return json;
	}
}
