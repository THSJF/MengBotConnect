package com.meng.botconnect.bean;

import com.meng.botconnect.*;
import com.meng.botconnect.lib.*;
import java.util.*;
import java.util.concurrent.*;

public class Group {

    public long id;
    public String name;
	public ConcurrentHashMap<Long,Member> memberSet=new ConcurrentHashMap<>();
	public ArrayList<BotMessage> messageList=new ArrayList<>();

    public Group() {
    }

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
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

	public Member getMenberByQQ(final long qq) {
		Member m=memberSet.get(qq);
		if (m == null) {
			m = new Member();
			m.setQqId(qq);
			m.setNick("" + qq);
			memberSet.put(qq, m);
			MainActivity2.instence.threadPool.execute(new Runnable(){

					@Override
					public void run() {
						Member m=MainActivity2.instence.cq.getGroupMemberInfo(id, qq);
						if (m != null) {
							addMember(m);
						}
					}
				});
		}
		return m;
	}

    @Override
    public String toString() {
        return String.format("Group{id=%d, name=%s", id, name);
    }
}
