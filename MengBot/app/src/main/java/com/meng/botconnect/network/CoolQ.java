package com.meng.botconnect.network;
import com.google.gson.reflect.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;
import java.lang.reflect.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.concurrent.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;

import com.meng.botconnect.bean.Member;


public class CoolQ extends WebSocketClient {
	private ConcurrentHashMap<Integer,BotDataPack> resultMap=new ConcurrentHashMap<>();
	public CoolQ() throws Exception {
		super(new URI("ws://123.207.65.93:7777"));
	}

	@Override
	public void onMessage(String p1) {
		LogTool.t(MainActivity2.instence, p1);
	}

	public void send(BotDataPack bdp) {
		send(bdp.getData());
	}

	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		LogTool.t(MainActivity2.instence, "连接到苗");
		send(BotDataPack.encode(BotDataPack.getConfig));
		MainActivity2.instence.threadPool.execute(new Runnable(){

				@Override
				public void run() {
					try {
						send(BotDataPack.encode(BotDataPack.heardBeat));
					} catch (WebsocketNotConnectedException e) {
						LogTool.e(MainActivity2.instence, e);
						reconnect();
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						LogTool.e(MainActivity2.instence, e);
					}
				}
			});
	}

	@Override
	public void onMessage(ByteBuffer bs) {	
		BotDataPack dataRec=BotDataPack.decode(bs.array());
		if (dataRec.getOpCode() == BotDataPack.onGroupMsg) {
			while (dataRec.hasNext()) {
				BotMessage bm=new BotMessage(dataRec.readInt(), dataRec.readLong(), dataRec.readLong(), dataRec.readString(), dataRec.readInt());
				MainActivity2.instence.addMsg(bm);
			}
		} else if (dataRec.getOpCode() == BotDataPack.getConfig) {
			Type type = new TypeToken<RanConfigBean>() {
			}.getType();
			MainActivity2.instence.botData.ranConfig = MainActivity2.instence.gson.fromJson(dataRec.readString(), type);
		} else {
			resultMap.put(dataRec.getOpCode(), dataRec);
		}
	}

	@Override
	public void onClose(int i, String s, boolean b) {
		LogTool.t(MainActivity2.instence, "连接断开");
	}

	@Override
	public void onError(Exception e) {
		LogTool.t(MainActivity2.instence, e);
	}

	private BotDataPack getTaskResult(int opCode) {
		int time=5000;
		while (resultMap.get(opCode) == null && time-- > 0) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
		BotDataPack tr=resultMap.get(opCode);
		resultMap.remove(opCode);
		return tr;
	}

    public long getLoginQQ() {
        send(BotDataPack.encode(BotDataPack.opLoginQQ));
		return getTaskResult(BotDataPack.opLoginQQ).readLong();

    }

    public String getLoginNick() {
		send(BotDataPack.encode(BotDataPack.opLoginNick));
		return getTaskResult(BotDataPack.opLoginNick).readString();
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
		MainActivity2.instence.addMsg(groupId, MainActivity2.onLoginQQ, msg);
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

    public String getCookies() {
		send(BotDataPack.encode(BotDataPack.opCookies));
		return getTaskResult(BotDataPack.opCookies).readString();
	}

    public int getCsrfToken() {
		send(BotDataPack.encode(BotDataPack.opCsrfToken));
		return getTaskResult(BotDataPack.opCsrfToken).readInt();
	}

    public String getRecord(String file, String outformat) {
		return null;
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

    public Member getGroupMemberInfo(final long groupId, final long qqId) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberInfo);
		bdp.write(groupId).write(qqId);
		send(bdp);
		LogTool.t(MainActivity2.instence, "获取member" + qqId);
		BotDataPack recData=getTaskResult(BotDataPack.opGroupMemberInfo);
		if (recData == null) {
			MainActivity2.instence.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						MainActivity2.instence.botData.getGroup(groupId). addMember(MainActivity2.instence.cq.getGroupMemberInfo(groupId, qqId));
					}
				});
			LogTool.t(MainActivity2.instence, "获取member失败" + qqId);
		} else {
			LogTool.t(MainActivity2.instence, "获取member成功" + qqId);
		}
		return new Member(
			recData.readLong(),
			recData.readLong(),
			recData.readString(),
			recData.readString(),
			recData.readInt(),
			recData.readInt(),
			recData.readString(),
			new Date(recData.readLong()),
			new Date(recData.readLong()),
			recData.readString(),
			recData.readInt(),
			recData.readString(),
			new Date(recData.readLong()),
			recData.readBoolean(),
			recData.readBoolean());
    }

	public Group getGroupInfo(long groupId) {
		LogTool.t(MainActivity2.instence, "获取group" + groupId);

		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberInfo);
		bdp.write(groupId);
		send(bdp);
		BotDataPack recData=getTaskResult(BotDataPack.opGroupMemberInfo);
		if (recData == null) {
			LogTool.t(MainActivity2.instence, "获取group失败" + groupId);
		} else {
			LogTool.t(MainActivity2.instence, "获取group成功" + groupId);
		}
		Group g = new Group(recData.readLong(), recData.readString());
		LogTool.t(MainActivity2.instence, g.toString());
		return g;
    }

    public void setDiscussLeave(long discussionId) {

	}

    public void setFriendAddRequest(String responseFlag, int backType, String remarks) {

	}

    public void setGroupAddRequestV2(String responseFlag, int requestType, int backType, String reason) {

	}

    public ArrayList<Member> getGroupMemberList(long groupId) {
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberList);
		bdp.write(groupId);
		send(bdp);
		BotDataPack recData=getTaskResult(BotDataPack.opGroupMemberList);
		ArrayList<Member> mlist=new ArrayList<>();
		while (recData.hasNext()) {
			mlist.add(new Member(
						  recData.readLong(),
						  recData.readLong(),
						  recData.readString(),
						  recData.readString(),
						  recData.readInt(),
						  recData.readInt(),
						  recData.readString(),
						  new Date(recData.readLong()),
						  new Date(recData.readLong()),
						  recData.readString(),
						  recData.readInt(),
						  recData.readString(),
						  new Date(recData.readLong()),
						  recData.readBoolean(),
						  recData.readBoolean()));
		}
		return mlist;
    }

    public ArrayList<Group> getGroupList() {
		send(BotDataPack.encode(BotDataPack.opGroupList));
		BotDataPack recData=getTaskResult(BotDataPack.opGroupList);
		ArrayList<Group> mlist=new ArrayList<>();
		while (recData.hasNext()) {
			Group m=new Group();
			m.id = recData.readLong();
			m.name = recData.readString();
			mlist.add(m);
		}
        return mlist;
    }

	/* public Anonymous getAnonymous(String source) {
	 return Anonymous.toAnonymous(base64Decode(source));
	 }
	 */
}

