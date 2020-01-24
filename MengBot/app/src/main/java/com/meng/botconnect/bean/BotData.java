package com.meng.botconnect.bean;
import java.util.*;

public class BotData {
	public ArrayList<Group> groupList=new ArrayList<>();

	public Group getGroup(long groupNumber) {
		for (Group g:groupList) {
			if (g.id == groupNumber) {
				return g;
			}
		}
		return null;
	}

	public Member getGroupMember(long group, long qq) {
		Group g=getGroup(group);
		if (g == null) {
			return null;
		}
		Member m=g.getMenberByQQ(qq);
		return m;
	}
}
