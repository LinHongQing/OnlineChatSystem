package cache;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import exception.IllegalEmptyParameterException;

public class OnlineUsersStorage {
	private static Map<String, Map<String, Session>> onlineUsers = new HashMap<String, Map<String, Session>>();
	
	public static synchronized boolean addOnlineUser(String fromUsrUid, String toUsrUid, Session session) throws IllegalEmptyParameterException {
		if (fromUsrUid == null || "".equals(fromUsrUid)) {
			throw new IllegalEmptyParameterException("fromUsrUid 非法!");
		}
		if (toUsrUid == null || "".equals(toUsrUid)) {
			throw new IllegalEmptyParameterException("toUsrUid 非法!");
		}
		if (onlineUsers.get(fromUsrUid) == null) {
			Map<String, Session> newHashStorage = new HashMap<String, Session>();
			onlineUsers.put(fromUsrUid, newHashStorage);
		}
		Map<String, Session> fromUser = onlineUsers.get(fromUsrUid);
		fromUser.put(toUsrUid, session);
		return true;
	}
	
	public static synchronized boolean delOnlineUser(String fromUsrUid, String toUsrUid) throws IllegalEmptyParameterException {
		if (fromUsrUid == null || "".equals(fromUsrUid)) {
			throw new IllegalEmptyParameterException("fromUsrUid 非法!");
		}
		if (toUsrUid == null || "".equals(toUsrUid)) {
			throw new IllegalEmptyParameterException("fromUsrUid 非法!");
		}
		Map<String, Session> toUsersHashMap = onlineUsers.get(fromUsrUid);
		if (toUsersHashMap == null) {
			return false;
		}
		if (toUsersHashMap.remove(toUsrUid) == null) {
			return false;
		}
		return true;
	}
	
	public static Session getOnlineUser(String fromUsrUid, String toUsrUid) throws IllegalEmptyParameterException {
		if (fromUsrUid == null || "".equals(fromUsrUid)) {
			throw new IllegalEmptyParameterException("fromUsrUid 非法!");
		}
		if (toUsrUid == null || "".equals(toUsrUid)) {
			throw new IllegalEmptyParameterException("toUsrUid 非法!");
		}
		return onlineUsers.get(toUsrUid) == null ? null : onlineUsers.get(toUsrUid).get(fromUsrUid);
	}
}
