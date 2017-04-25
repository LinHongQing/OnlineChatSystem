package service;

import bean.SystemConfigurationInfo;
import exception.IllegalEmptyParameterException;
import util.HttpRequestUtil;
import util.JSONUtil;

public class NotifyUnreadInfoAction {
	public static boolean doNotify(String fromUsrUid, String toUsrUid, int count) {
		try {
			if (fromUsrUid == null || toUsrUid == null || "".equals(fromUsrUid) || "".equals(toUsrUid)) {
				throw new IllegalEmptyParameterException("传入参数非法!");
			}
			String initUrl = SystemConfigurationInfo.getMainSystemBasePath() + "systemapis.action";
			String initParam = "which=notifyUnreadMessage&from=" + fromUsrUid + "&to=" + toUsrUid + "&count=" + count;
			String apiReply = HttpRequestUtil.sendPost(initUrl, initParam);
			JSONUtil json = new JSONUtil(apiReply);
			if ("success".equals(json.resolveJSONString("msgType"))) {
				return true;
			} else {
				return false;
			}
		} catch (IllegalEmptyParameterException e) {
			e.printStackTrace();
			return false;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
}
