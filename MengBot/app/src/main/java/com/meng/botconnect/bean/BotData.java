package com.meng.botconnect.bean;
import java.util.*;
import com.meng.botconnect.*;

public class BotData {
	public ArrayList<Group> groupList=new ArrayList<>();
	public RanConfigBean ranConfig=new RanConfigBean();

	public Group getGroup(long groupNumber) {
		for (Group g:groupList) {
			if (g.id == groupNumber) {
				return g;
			}
		}
		return null;
	}

	public void addGroup(Group gToPut) {
		for (Group g:groupList) {
			if (g.id == gToPut.id) {
				g.name = gToPut.name;
				return;
			}
		}
		groupList.add(gToPut);
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
