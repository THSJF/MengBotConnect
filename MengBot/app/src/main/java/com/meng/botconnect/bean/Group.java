package com.meng.botconnect.bean;

import java.util.*;

public class Group {

    private long id;
    private String name;
	private HashSet<Member> memberSet=new HashSet<>();

    public Group() {
    }

    public Group(long id, String name) {
        this.id = id;
        this.name = name;
    }

	public void addMember(Member m) {
		memberSet.add(m);
	}

	public Member getMenberByQQ(long qq) {
		for (Member m:memberSet) {
			if (m.getQqId() == qq) {
				return m;
			}
		}
		return null;
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Group{id=%d, name=%s", id, name);
    }
}
