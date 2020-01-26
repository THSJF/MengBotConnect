package com.meng.botconnect.network;
import com.google.gson.reflect.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;

import com.meng.botconnect.bean.Member;


public class CoolQ extends WebSocketClient {

	public static String ip=null;
	public static String port=null;

	public CoolQ() throws Exception {
		super(new URI(String.format("ws://%s:%s", ip, port)));
	}

	@Override
	public void onMessage(String p1) {
		LogTool.t(MainActivity2.instance, p1);
	}

	public void send(BotDataPack bdp) {
		send(bdp.getData());
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		LogTool.t(MainActivity2.instance, "连接到server");
		send(BotDataPack.encode(BotDataPack.getConfig));
		send(BotDataPack.encode(BotDataPack.opLoginNick));
		send(BotDataPack.encode(BotDataPack.opLoginQQ));
		send(BotDataPack.encode(BotDataPack.opCookies));
		send(BotDataPack.encode(BotDataPack.opGroupList));
		MainActivity2.instance.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						send(BotDataPack.encode(BotDataPack.heardBeat));
					} catch (WebsocketNotConnectedException e) {
						LogTool.e(MainActivity2.instance, e);
						reconnect();
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						LogTool.e(MainActivity2.instance, e);
					}
				}
			});
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		BotDataPack received=BotDataPack.decode(bs.array());
		if (BotDataPack.onGroupMsg == received.getOpCode()) {
			while (received.hasNext()) {
				MainActivity2.instance.addMsg(new BotMessage(received.readInt(), received.readLong(), received.readLong(), received.readString(), received.readInt()));
			}
		} else {
			switch (received.getOpCode()) {
				case BotDataPack.opLoginQQ:
					MainActivity2.nowBot.setOnLoginQQ(received.readLong());
					break;
				case BotDataPack.opLoginNick:
					MainActivity2.nowBot.setOnLoginNick(received.readString());
					break;
				case BotDataPack.opCookies:
					MainActivity2.nowBot.setCookie(received.readString());
					break;
				case BotDataPack.opCsrfToken:
					MainActivity2.nowBot.setCsrfToken(received.readInt());
					break;
				case BotDataPack.opGroupMemberInfo:
					Member m=new Member(
						received.readLong(),
						received.readLong(),
						received.readString(),
						received.readString(),
						received.readInt(),
						received.readInt(),
						received.readString(),
						new Date(received.readLong()),
						new Date(received.readLong()),
						received.readString(),
						received.readInt(),
						received.readString(),
						new Date(received.readLong()),
						received.readBoolean(),
						received.readBoolean());
					MainActivity2.instance.addGroupMember(m);
					break;
					/*	case BotDataPack.opFriendAddRequest:
					 toSend = BotDataPack.encode(rec.getOpCode());
					 break;*/
				case BotDataPack.opGroupMemberList:
					int mc=0;
					while (received.hasNext()) {
						Member m2=new Member(
							received.readLong(),
							received.readLong(),
							received.readString(),
							received.readString(),
							received.readInt(),
							received.readInt(),
							received.readString(),
							new Date(received.readLong()),
							new Date(received.readLong()),
							received.readString(),
							received.readInt(),
							received.readString(),
							new Date(received.readLong()),
							received.readBoolean(),
							received.readBoolean());
						MainActivity2.instance.addGroupMember(m2);
						++mc;
					}
					LogTool.t(MainActivity2.instance, "获得了" + mc + "个用户");
					break;
				case BotDataPack.opGroupList:
					int gc=0;
					while (received.hasNext()) {
						Group g=new Group();
						g.id = received.readLong();
						g.name = received.readString();
						MainActivity2.instance.botData.addGroup(g);
						++gc;
					}
					MainActivity2.nowBot.setGroupCount(gc);
					break;
				case BotDataPack.getConfig:
					MainActivity2.instance.botData.ranConfig = MainActivity2.instance.gson.fromJson(received.readString(), new TypeToken<RanConfigBean>() {}.getType());
					break;
			}

		}


		/*if (dataRec.getOpCode() == BotDataPack.getConfig) {
		 Type type = new TypeToken<RanConfigBean>() {
		 }.getType();
		 MainActivity2.instence.botData.ranConfig = MainActivity2.instence.gson.fromJson(dataRec.readString(), type);
		 } else {
		 resultMap.put(dataRec.getOpCode(), dataRec);
		 }*/
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		LogTool.t(MainActivity2.instance, "连接断开");
	}

	@Override
	public void onError(Exception e) {
		LogTool.t(MainActivity2.instance, e);
	}

    public void getLoginQQ() {
        send(BotDataPack.encode(BotDataPack.opLoginQQ));
	}

    public void getLoginNick() {
		send(BotDataPack.encode(BotDataPack.opLoginNick));
	}

    public void sendPrivateMsg(long qqId, String msg) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opPrivateMsg);
		bdp.write(qqId).write(msg);
		send(bdp);
	}

    public void sendGroupMsg(long groupId, String msg) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMsg);
		bdp.write(groupId).write(msg);
		send(bdp);
		MainActivity2.instance.addMsg(groupId, MainActivity2.nowBot.getOnLoginQQ(), msg);
	}

    public void sendDiscussMsg(long discussionId, String msg) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opDiscussMsg);
		bdp.write(discussionId).write(msg);
		send(bdp);
	}

    public void deleteMsg(long msgId) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opDeleteMsg);
		bdp.write(msgId);
		send(bdp);
	}

    public void sendLike(long qqId, int times) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opSendLike);
		bdp.write(qqId).write(times);
		send(bdp);
	}

    public void getCookies() {
		send(BotDataPack.encode(BotDataPack.opCookies));
	}

    public void getCsrfToken() {
		send(BotDataPack.encode(BotDataPack.opCsrfToken));
	}

    public void getRecord(String file, String outformat) {
		throw new RuntimeException("未实现的方法");
	}

    public void setGroupKick(long groupId, long qqId, boolean notBack) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupKick);
		bdp.write(groupId).write(qqId).write(notBack);
		send(bdp);
	}

    public void setGroupBan(long groupId, long qqId, long banTime) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupBan);
		bdp.write(groupId).write(qqId).write(banTime);
		send(bdp);
	}

    public void setGroupAdmin(long groupId, long qqId, boolean isAdmin) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupAdmin);
		bdp.write(groupId).write(qqId).write(isAdmin);
		send(bdp);
	}

    public void setGroupWholeBan(long groupId, boolean isBan) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupWholeBan);
		bdp.write(groupId).write(isBan);
		send(bdp);
	}

    public void setGroupAnonymousBan(long groupId, String anonymous, long banTime) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupAnonymousBan);
		bdp.write(groupId).write(anonymous).write(banTime);
		send(bdp);
	}

    public void setGroupAnonymous(long groupId, boolean isAnonymous) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupAnonymous);
		bdp.write(groupId).write(isAnonymous);
		send(bdp);
	}

    public void setGroupCard(long groupId, long qqId, String nick) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupCard);
		bdp.write(groupId).write(qqId).write(nick);
		send(bdp);
	}

    public void setGroupLeave(long groupId, boolean isDisband) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupLeave);
		bdp.write(groupId).write(isDisband);
		send(bdp);
	}

    public void setGroupSpecialTitle(long groupId, long qqId, String title, long expireTime) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupSpecialTitle);
		bdp.write(groupId).write(qqId).write(title).write(expireTime);
		send(bdp);
	}

    public void getGroupMemberInfo(long groupId, long qqId) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberInfo);
		bdp.write(groupId).write(qqId);
		send(bdp);
	}

    public void setDiscussLeave(long discussionId) {

	}

    public void setFriendAddRequest(String responseFlag, int backType, String remarks) {

	}

    public void setGroupAddRequestV2(String responseFlag, int requestType, int backType, String reason) {

	}

    public void getGroupMemberList(long groupId) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberList);
		bdp.write(groupId);
		send(bdp);
    }

    public void getGroupList() {
		send(BotDataPack.encode(BotDataPack.opGroupList));
    }

	/* public Anonymous getAnonymous(String source) {
	 return Anonymous.toAnonymous(base64Decode(source));
	 }
	 */
}

