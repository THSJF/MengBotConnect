package com.meng.botconnect.bean;

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

	public Member getMenberByQQ(long qq) {
		return memberSet.get(qq);
	}

    @Override
    public String toString() {
        return String.format("Group{id=%d, name=%s", id, name);
    }
}
