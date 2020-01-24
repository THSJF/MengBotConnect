package com.meng.botconnect.bean;

import com.meng.botconnect.*;

public class BotMessage {
	private byte type=-1;
	private long group;
	private long fromQQ;
	private String msg=null;
	private int msgId=-1;

	public BotMessage(byte type, long group, long fromQQ, String msg, int msgId) {
		this.type = type;
		this.group = group;
		this.fromQQ = fromQQ;
		this.msg = msg;
		this.msgId = msgId;
	}

	public String getUserName() {
		for (Group g:MainActivity2.instence.groupList) {
			if (g.getId() == group) {
				Member m = g.getMenberByQQ(fromQQ);
				if (m == null) {
					return null;
				}
				return m.getNick();
			}
		}
		return null;
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

	public byte getType() {
		return type;
	}
}
