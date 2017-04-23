package cache;

import java.util.HashMap;
import java.util.Map;

import javax.websocket.Session;

import exception.IllegalEmptyStringException;

public class OnlineUsers {
	private static Map<String, Session> onlineUsers = new HashMap<String, Session>();
	
	public static synchronized boolean addOnlineUser(String usrUid, Session session) throws IllegalEmptyStringException {
		if (usrUid == null || "".equals(usrUid)) {
			throw new IllegalEmptyStringException("usrUid 非法!");
		}
		onlineUsers.put(usrUid, session);
		return true;
	}
	
	public static synchronized boolean delOnlineUser(String usrUid) throws IllegalEmptyStringException {
		if (usrUid == null || "".equals(usrUid)) {
			throw new IllegalEmptyStringException("usrUid 非法!");
		}
		if (onlineUsers.remove(usrUid) == null) {
			return false;
		}
		return true;
	}
	
	public static Session getOnlineUser(String usrUid) throws IllegalEmptyStringException {
		if (usrUid == null || "".equals(usrUid)) {
			throw new IllegalEmptyStringException("usrUid 非法!");
		}
		return onlineUsers.get(usrUid);
	}
}
