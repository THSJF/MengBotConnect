package com.meng.botconnect.bean;

import android.app.*;
import com.meng.botconnect.*;
import com.meng.botconnect.fragment.*;
import java.util.*;
import java.util.concurrent.*;

public class Group {

    public long id;
    public String name;
	private ConcurrentHashMap<Long,Member> memberSet=new ConcurrentHashMap<>();
	private ArrayList<BotMessage> messageList=new ArrayList<>();

	public HashSet<Long> onGettingInfo=new HashSet<>();
    public Group() {
    }

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

	public void addMessage(BotMessage bm) {
		messageList.add(bm);
	}

	public ArrayList<BotMessage> getMessageList() {
		return messageList;
	}

	public void addMember(Member mToAdd) {
		for (Member m:memberSet.values()) {
			if (m.getQqId() == mToAdd.getQqId()) {
				memberSet.put(m.getQqId(), mToAdd);
				return;
			}
		}
		memberSet.put(mToAdd.getQqId(), mToAdd);
	}

	public Member getMenberByQQ(long qq) {
		Member m=memberSet.get(qq);
		if (m == null && !onGettingInfo.contains(qq)) {
			MainActivity2.instance.CQ.getGroupMemberInfo(id, qq);
			onGettingInfo.add(qq);
		}
		return m;
	}

    @Override
    public String toString() {
        return String.format("Group{id=%d, name=%s", id, name);
    }
}
