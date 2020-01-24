package com.meng.botconnect.bean;

import com.meng.botconnect.*;
import com.meng.botconnect.lib.*;

public class BotMessage {
	private int type=-1;
	private long group;
	private long fromQQ;
	private String msg=null;
	private int msgId=-1;

	public BotMessage(int type, long group, long fromQQ, String msg, int msgId) {
		this.type = type;
		this.group = group;
		this.fromQQ = fromQQ;
		this.msg = msg;
		this.msgId = msgId;
	}

	public String getUserName() {
		String nick=null;
		nick = MainActivity2.instence.botData.ranConfig.getNickName(group, fromQQ);
		if (nick != null) {
			return nick;
		}
		Member m=MainActivity2.instence.botData.getGroupMember(group, fromQQ);
		if (m == null) {
			return null;
		}
		return m.getNick();
	}

	public int getMsgId() {
		return msgId;
	}

	public String getMsg() {
		return msg;
	}

	public long getFromQQ() {
		return fromQQ;
	}

	public long getGroup() {
		return group;
	}

	public int getType() {
		return type;
	}
}
