package com.meng.botconnect.network;
import android.os.*;
import android.widget.*;
import com.google.gson.reflect.*;
import com.meng.botconnect.*;
import com.meng.botconnect.bean.*;
import com.meng.botconnect.lib.*;
import com.meng.grzxConfig.MaterialDesign.adapters.*;
import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import org.java_websocket.client.*;
import org.java_websocket.exceptions.*;
import org.java_websocket.handshake.*;


public class CoolQ extends WebSocketClient {

	String folder=Environment.getExternalStorageDirectory() + "/pictures/sanae/questions/";

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
		final BotDataPack rec=BotDataPack.decode(bs.array());
		if (BotDataPack.onGroupMsg == rec.getOpCode()) {
			while (rec.hasNext()) {
				MainActivity2.instance.addMsg(new BotMessage(rec.readInt(), rec.readLong(), rec.readLong(), rec.readString(), rec.readInt()));
			}
		} else {
			switch (rec.getOpCode()) {
				case BotDataPack.opLoginQQ:
					MainActivity2.nowBot.setOnLoginQQ(rec.readLong());
					break;
				case BotDataPack.opLoginNick:
					MainActivity2.nowBot.setOnLoginNick(rec.readString());
					break;
				case BotDataPack.opCookies:
					MainActivity2.nowBot.setCookie(rec.readString());
					break;
				case BotDataPack.opCsrfToken:
					MainActivity2.nowBot.setCsrfToken(rec.readInt());
					break;
				case BotDataPack.opGroupMemberInfo:
					Member m=new Member(
						rec.readLong(),
						rec.readLong(),
						rec.readString(),
						rec.readString(),
						rec.readInt(),
						rec.readInt(),
						rec.readString(),
						new Date(rec.readLong()),
						new Date(rec.readLong()),
						rec.readString(),
						rec.readInt(),
						rec.readString(),
						new Date(rec.readLong()),
						rec.readBoolean(),
						rec.readBoolean());
					MainActivity2.instance.addGroupMember(m);
					break;
					/*	case BotDataPack.opFriendAddRequest:
					 toSend = BotDataPack.encode(rec.getOpCode());
					 break;*/
				case BotDataPack.opGroupMemberList:
					int mc=0;
					while (rec.hasNext()) {
						Member m2=new Member(
							rec.readLong(),
							rec.readLong(),
							rec.readString(),
							rec.readString(),
							rec.readInt(),
							rec.readInt(),
							rec.readString(),
							new Date(rec.readLong()),
							new Date(rec.readLong()),
							rec.readString(),
							rec.readInt(),
							rec.readString(),
							new Date(rec.readLong()),
							rec.readBoolean(),
							rec.readBoolean());
						MainActivity2.instance.addGroupMember(m2);
						++mc;
					}
					LogTool.t(MainActivity2.instance, "获得了" + mc + "个用户");
					break;
				case BotDataPack.opGroupList:
					int gc=0;
					while (rec.hasNext()) {
						Group g=new Group();
						g.id = rec.readLong();
						g.name = rec.readString();
						MainActivity2.instance.botData.addGroup(g);
						++gc;
					}
					MainActivity2.nowBot.setGroupCount(gc);
					break;
				case BotDataPack.getConfig:
					RanConfigBean rcfgb=MainActivity2.instance.gson.fromJson(rec.readString(), new TypeToken<RanConfigBean>() {}.getType());
					MainActivity2.instance.botData.ranConfig = rcfgb;

					MainActivity2.instance.groupConfigAdapter = new GroupConfigAdapter(MainActivity2.instance, rcfgb.groupConfigs);
					MainActivity2.instance.qqNotReplyAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.QQNotReply);
					MainActivity2.instance.personInfoAdapter = new PersonInfoAdapter(MainActivity2.instance, rcfgb.personInfo);
					MainActivity2.instance.wordNotReplyAdapter = new ArrayAdapter<>(MainActivity2.instance, android.R.layout.simple_list_item_1, rcfgb.wordNotReply);
					MainActivity2.instance.masterAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.masterList);
					MainActivity2.instance.adminAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.adminList);
					MainActivity2.instance.groupAutoAllowAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.groupAutoAllowList);
					MainActivity2.instance.blackQQAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.blackListQQ);
					MainActivity2.instance.blackGroupAdapter = new QQAccountAdapter(MainActivity2.instance, rcfgb.blackListGroup, true);

					MainActivity2.instance.groupConfigFragment.mListView.setAdapter(MainActivity2.instance.groupConfigAdapter);
					MainActivity2.instance.qqNotReplyFragment.mListView.setAdapter(MainActivity2.instance.qqNotReplyAdapter);
					MainActivity2.instance.wordNotReplyFragment.mListView.setAdapter(MainActivity2.instance.wordNotReplyAdapter);
					MainActivity2.instance.personInfoFragment.mListView.setAdapter(MainActivity2.instance.personInfoAdapter);
					MainActivity2.instance.masterFragment.mListView.setAdapter(MainActivity2.instance.masterAdapter);
					MainActivity2.instance.adminFragment.mListView.setAdapter(MainActivity2.instance.adminAdapter);
					MainActivity2.instance.groupAutoAllowFragment.mListView.setAdapter(MainActivity2.instance.groupAutoAllowAdapter);
					MainActivity2.instance.blackQQFragment.mListView.setAdapter(MainActivity2.instance.blackQQAdapter);
					MainActivity2.instance.blackGroupFragment.mListView.setAdapter(MainActivity2.instance.blackGroupAdapter);

					break;
				case BotDataPack.onPerSecMsgInfo:
					MainActivity2.instance.nowBot.setMsgInfo(rec.readInt(), rec.readInt(), rec.readInt(), rec.readInt(), rec.readInt());
					break;
				case BotDataPack.opTextNotify:
					LogTool.t(MainActivity2.instance, rec.readString());
					break;
				case BotDataPack.opAllQuestion:
					MainActivity2.instance.quesFragment.alAllQa.clear();
					readQAs(rec);
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.quesFragment.quesAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.opQuestionPic:
					File ffo=new File(folder);
					if (!ffo.exists()) {
						ffo.mkdirs();
					}
					final int id=rec.readInt();
					new Thread(new Runnable(){

							@Override
							public void run() {
								rec.readFile(new File(folder + id + ".jpg"));
							}
						}).start();
					break;
				case BotDataPack.opEnableFunction:
					MainActivity2.instance.botData.ranConfig.getGroupConfig(rec.readLong()).setFunctionEnabled(rec.readInt(), rec.readInt() == 1);
					break;
				case BotDataPack.addGroup:
					GroupConfig g1c=new GroupConfig();
					g1c.groupNumber = rec.readLong();
					MainActivity2.instance.botData.ranConfig.groupConfigs.add(g1c);
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {		
								MainActivity2.instance.groupConfigAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addNotReplyUser:
					MainActivity2.instance.botData.ranConfig.QQNotReply.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {	
								MainActivity2.instance.qqNotReplyAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addNotReplyWord:
					MainActivity2.instance.botData.ranConfig.wordNotReply.add(rec.readString());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.wordNotReplyAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addPersonInfo:
					MainActivity2.instance.botData.ranConfig.personInfo.add(MainActivity2.instance.gson.fromJson(rec.readString(), PersonInfo.class));
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.personInfoAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addMaster:
					MainActivity2.instance.botData.ranConfig.masterList.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.masterAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addAdmin:
					MainActivity2.instance.botData.ranConfig.adminList.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.adminAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addGroupAllow:
					MainActivity2.instance.botData.ranConfig.groupAutoAllowList.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.groupAutoAllowAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addBlackQQ:
					MainActivity2.instance.botData.ranConfig.blackListQQ.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.blackQQAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.addBlackGroup:
					MainActivity2.instance.botData.ranConfig.blackListGroup.add(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.blackGroupAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeGroup:
					long gcn=rec.readLong();
					Iterator<GroupConfig> iterator=MainActivity2.instance.botData.ranConfig.groupConfigs.iterator();
					while (iterator.hasNext()) {
						GroupConfig gcr=iterator.next();
						if (gcr.groupNumber == gcn) {
							iterator.remove();
							break;
						}
					}
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.groupConfigAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeNotReplyUser:
					MainActivity2.instance.botData.ranConfig.QQNotReply.remove(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.qqNotReplyAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeNotReplyWord:
					MainActivity2.instance.botData.ranConfig.wordNotReply.remove(rec.readString());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.wordNotReplyAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removePersonInfo:
					MainActivity2.instance.botData.ranConfig.personInfo.remove(MainActivity2.instance.gson.fromJson(rec.readString(), PersonInfo.class));
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.personInfoAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeMaster:
					long rm=rec.readLong();
					MainActivity2.instance.botData.ranConfig.masterList.remove(rm);
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.masterAdapter.notifyDataSetChanged();	
							}
						});
					break;
				case BotDataPack.removeAdmin:
					long ra=rec.readLong();
					MainActivity2.instance.botData.ranConfig.adminList.remove(ra);
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.adminAdapter.notifyDataSetChanged();	
							}
						});
					break;
				case BotDataPack.removeGroupAllow:
					MainActivity2.instance.botData.ranConfig.groupAutoAllowList.remove(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.groupAutoAllowAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeBlackQQ:
					MainActivity2.instance.botData.ranConfig.blackListQQ.remove(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.blackQQAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.removeBlackGroup:
					MainActivity2.instance.botData.ranConfig.blackListGroup.remove(rec.readLong());
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.blackGroupAdapter.notifyDataSetChanged();
							}
						});
					break;
				case BotDataPack.setPersonInfo:
					PersonInfo oldPersonInfo = MainActivity2.instance.gson.fromJson(rec.readString(), PersonInfo.class);
					PersonInfo newPersonInfo = MainActivity2.instance.gson.fromJson(rec.readString(), PersonInfo.class);
					for (PersonInfo pi : MainActivity2.instance.botData.ranConfig.personInfo) {
						if (pi.name.equals(oldPersonInfo.name) && pi.qq == oldPersonInfo.qq && pi.bid == oldPersonInfo.bid && pi.bliveRoom == oldPersonInfo.bliveRoom) {
							MainActivity2.instance.botData.ranConfig.personInfo.remove(oldPersonInfo);
							break;
						}
					}
					MainActivity2.instance.botData.ranConfig.personInfo.add(newPersonInfo);
					MainActivity2.instance.runOnUiThread(new Runnable(){

							@Override
							public void run() {
								MainActivity2.instance.personInfoAdapter.notifyDataSetChanged();
							}
						});
					break;	

					/*case BotDataPack.getCqImgFile:
					 File f=new File(MainActivity2.mainFolder + "/cqimg/" + rec.readString());
					 rec.readFile(f);
					 /*	File iniFile = new File(path);
					 if (iniFile.exists() && iniFile.canRead()){
					 try {
					 new CQImage(new IniFile(iniFile));
					 } catch (IOException e) {}
					 } else {
					 LogTool.i(MainActivity2.instance,file+".cqimg不存在");
					 }
					 break;*/
			}
		}
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
		LogTool.t(MainActivity2.instance, "获取用户列表:" + groupId);
		BotDataPack bdp=BotDataPack.encode(BotDataPack.opGroupMemberList);
		bdp.write(groupId);
		send(bdp);
    }

    public void getGroupList() {
		send(BotDataPack.encode(BotDataPack.opGroupList));
    }

	private void readQAs(BotDataPack sdp) {
		while (sdp.hasNext()) {
			QA qa=new QA();
			qa.setFlag(sdp.readInt());
			qa.l = sdp.readInt();
			qa.q = sdp.readString();
			File img=new File(folder + qa.getId() + ".jpg");
			if (qa.q.contains("(image)")) {
				if (!img.exists() || (int)img.length() != qa.l) {
					BotDataPack sa=BotDataPack.encode(BotDataPack.opQuestionPic);
					sa.write(qa.getId());
					send(sa.getData());
				}
			}
			int anss=sdp.readInt();
			qa.setTrueAnsFlag(sdp.readInt());
			for (int i=0;i < anss;++i) {
				qa.a.add(sdp.readString());
			}
			qa.r = sdp.readString();
			MainActivity2.instance.quesFragment.alAllQa.add(qa);
		}
	}

	/*public void getCqImgFile(String fileName) {
	 send(BotDataPack.encode(BotDataPack.getCqImgFile).write(fileName));
	 }*/

	/* public Anonymous getAnonymous(String source) {
	 return Anonymous.toAnonymous(base64Decode(source));
	 }
	 */
}

