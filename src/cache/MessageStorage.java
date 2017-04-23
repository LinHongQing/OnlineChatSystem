package cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.ChatInfo;

public class MessageStorage {
	private static Map<String, Map<String, List<ChatInfo>>> storage = new HashMap<String, Map<String, List<ChatInfo>>>();
	
	/**
	 * 储存未发送成功的消息
	 * @param fromUsrUid 发送者通用 id;
	 * @param toUsrUid 接收者通用 id;
	 * @param chatInfo 聊天内容对象 List 数组;
	 * @return 储存结果, 成功为 true, 失败为 false.
	 */
	public static synchronized boolean storeUnsendMessage(String fromUsrUid, String toUsrUid, ChatInfo chatInfo) {
		try {
			Map<String, List<ChatInfo>> selectedUser = storage.get(toUsrUid);
			List<ChatInfo> chatList;
			if (selectedUser == null) {
				chatList = new ArrayList<ChatInfo>();
				chatList.add(chatInfo);
				selectedUser = new HashMap<String, List<ChatInfo>>();
				selectedUser.put(fromUsrUid, chatList);
				storage.put(toUsrUid, selectedUser);
			} else {
				chatList = selectedUser.get(fromUsrUid);
				if (chatList != null) {
					chatList.add(chatInfo);
				} else {
					chatList = new ArrayList<ChatInfo>();
					chatList.add(chatInfo);
					selectedUser.put(fromUsrUid, chatList);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		return true;
	}
	
	/**
	 * 获取储存的未发送成功的消息
	 * @param toUsrUid 接收者通用 id;
	 * @param fromUsrUid 发送者通用 id;
	 * @return 聊天内容对象 List 数组.
	 */
	public static synchronized List<ChatInfo> getUnreceivedMessage(String toUsrUid, String fromUsrUid) {
		try {
			if (storage.get(fromUsrUid) == null)
				return null;
			return storage.get(fromUsrUid).get(toUsrUid);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 删除储存的未发送的消息
	 * @param toUsrUid 接收者通用 id;
	 * @param fromUsrUid 发送者通用 id;
	 * @return 删除结果, 成功为 true, 失败为 false.
	 */
	public static synchronized boolean eraseStoreMessage(String toUsrUid, String fromUsrUid) {
		try {
			storage.get(fromUsrUid).remove(toUsrUid);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
}
