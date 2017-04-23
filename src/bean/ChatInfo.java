package bean;

public class ChatInfo {
	private boolean isSelf;
	private String msgTime;
	private String msgContent;
	private String nickName;
	private String toUserUid;
	public boolean isSelf() {
		return isSelf;
	}
	public void setSelf(boolean isSelf) {
		this.isSelf = isSelf;
	}
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	public String getMsgContent() {
		return msgContent;
	}
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getToUserUid() {
		return toUserUid;
	}
	public void setToUserUid(String toUserUid) {
		this.toUserUid = toUserUid;
	}
	@Override
	public String toString() {
		return "ChatInfo [isSelf=" + isSelf + ", msgTime=" + msgTime
				+ ", msgContent=" + msgContent + ", nickName=" + nickName
				+ ", toUserUid=" + toUserUid + "]";
	}
}
