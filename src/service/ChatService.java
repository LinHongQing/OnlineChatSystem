package service;

import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.SendHandler;
import javax.websocket.SendResult;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.google.gson.Gson;

import bean.ChatInfo;
import cache.MessageStorage;
import cache.OnlineUsersStorage;
import exception.IllegalEmptyParameterException;
import util.TimeUtil;

@ServerEndpoint("/chat/{usrUid}/{toUsrUid}")
public class ChatService {
	
	private String fromUsrUid;	// 发送者通用 id
	private String toUsrUid;	// 接收者通用 id
	private class MsgSendResultHandler implements SendHandler {	// 消息发送结果处理私有类
		String fromUsrUid;
		String toUsrUid;
		ChatInfo chatInfo;
		
		public MsgSendResultHandler(String fromUsrUid, String toUsrUid, ChatInfo chatInfo) {
			super();
			this.fromUsrUid = fromUsrUid;
			this.toUsrUid = toUsrUid;
			this.chatInfo = chatInfo;
		}

		@Override
		public void onResult(SendResult sr) {
			// TODO Auto-generated method stub
			if (!sr.isOK()) {
				System.out.println("消息发送失败!");
				System.out.println(sr.getException().getLocalizedMessage());
				try {
					MessageStorage.storeUnsendMessage(fromUsrUid, toUsrUid, chatInfo);
				} catch (IllegalEmptyParameterException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println("消息发送成功!");
			}
		}
		
	}

	/**
	 * 用户接入
	 * @param session 用户接入的 session
	 * @param fromUsrUid 发送者通用 id
	 * @param toUsrUid 接收者通用 id
	 */
	@OnOpen
	
	public void onOpen(@PathParam("usrUid") String fromUsrUid, @PathParam("toUsrUid") String toUsrUid, Session session){
		this.fromUsrUid = fromUsrUid;
		this.toUsrUid = toUsrUid;
		try {
			OnlineUsersStorage.addOnlineUser(this.fromUsrUid, this.toUsrUid, session);	//将接入用户 session 添加到在线表中
			
			List<ChatInfo> unreceivedMessages = MessageStorage.getUnreceivedMessage(toUsrUid, fromUsrUid);	//获取当前用户暂未接收的消息
			if (unreceivedMessages != null && !unreceivedMessages.isEmpty()) {	//有未接收的消息
				MessageStorage.eraseStoreMessage(toUsrUid, fromUsrUid);
				System.out.println("有未发送的消息!");
				Gson gson = new Gson();
				session.getAsyncRemote().sendText(gson.toJson(unreceivedMessages));	//发送消息给用户
			}
		} catch (IllegalEmptyParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 接收到来自用户的消息
	 * @param message 接收到的消息内容
	 * @param session 用户的 session
	 */
	@OnMessage
	public void onMessage(String message, Session session) {
		
		Gson gson = new Gson();
		//把用户发来的 json 消息解析为 ChatInfo 对象
		ChatInfo chatInfo = gson.fromJson(message, ChatInfo.class);
		try {
			//设置消息发送时间
			chatInfo.setMsgTime(TimeUtil.TimeStamp2Date(TimeUtil.getNowTimeStamp()));
			//设置消息是否为自己的
			if (fromUsrUid.equals(chatInfo.getToUserUid()))
				chatInfo.setSelf(false);
			else
				chatInfo.setSelf(true);
			//发送消息给发送者
			session.getAsyncRemote().sendText(gson.toJson(chatInfo), new MsgSendResultHandler(fromUsrUid, toUsrUid, chatInfo));
			//从 cache 中获取接收者的 session
			Session targetSession = OnlineUsersStorage.getOnlineUser(fromUsrUid, toUsrUid);

			if (targetSession == null) {//若接收者未在线, 将消息储存
				//设置消息为他人的
				chatInfo.setSelf(false);
				MessageStorage.storeUnsendMessage(fromUsrUid, toUsrUid, chatInfo);
			} else {//发送消息给接收者
				//设置消息为他人的
				chatInfo.setSelf(false);
				targetSession.getAsyncRemote().sendText(gson.toJson(chatInfo), new MsgSendResultHandler(fromUsrUid, toUsrUid, chatInfo));
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalEmptyParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/**
	 * 用户断开
	 * @param session
	 */
	@OnClose
	public void onClose(Session session){
		try {
			OnlineUsersStorage.delOnlineUser(fromUsrUid, toUsrUid);
		} catch (IllegalEmptyParameterException e) {
			// TODO Auto-generated catch block
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * 用户连接异常
	 * @param t
	 */
	@OnError
	public void onError(Session session, Throwable t){
		System.out.println(t.getMessage());
	}
}
