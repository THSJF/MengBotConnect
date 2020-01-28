package com.meng.botconnect.bean;

import com.meng.botconnect.*;
import java.util.*;

public class RanConfigBean {
	//public HashSet<GroupConfig> groupConfigs = new HashSet<>();
    public HashSet<Long> QQNotReply = new HashSet<>();
    public HashSet<Long> blackListQQ = new HashSet<>();
    public HashSet<Long> blackListGroup = new HashSet<>();
    public HashSet<String> wordNotReply = new HashSet<>();
    public HashSet<PersonInfo> personInfo = new HashSet<>();
    public HashSet<Long> masterList = new HashSet<>();
    public HashSet<Long> adminList = new HashSet<>();
    //public HashSet<Long> groupAutoAllowList = new HashSet<>();
	public HashMap<Long,String> nicknameMap = new HashMap<>();
	//public long ogg = 0;



	public String getNickName(long group, long qq) {
		String nick=null;
		nick = nicknameMap.get(qq);
		if (nick == null) {
			PersonInfo pi=getPersonInfoFromQQ(qq);
			if (pi == null) {
				Member m=MainActivity2.instance.botData.getGroupMember(group, qq);
				if (m == null) {
					return null;
				}
				return m.getNick();
			} else {
				nick = pi.name;
			}
		}
		return nick;
	}

    public boolean isMaster(long fromQQ) {
        return fromQQ == 1594703250L || fromQQ == 2856986197L || fromQQ == 8255053L || fromQQ == 1592608126L || fromQQ == 1620628713L || fromQQ == 2565128043L;
    }

    public boolean isAdmin(long fromQQ) {
        return adminList.contains(fromQQ) || isMaster(fromQQ);
    }

    public boolean isNotReplyQQ(long qq) {
        return QQNotReply.contains(qq) || blackListQQ.contains(qq);
    }

    public boolean isBlackQQ(long qq) {
        return blackListQQ.contains(qq);
    }

    public boolean isBlackGroup(long qq) {
        return blackListGroup.contains(qq);
    }

    public boolean isNotReplyWord(String word) {
        for (String nrw : wordNotReply) {
            if (word.contains(nrw)) {
                return true;
            }
        }
        return false;
    }

    public PersonInfo getPersonInfoFromQQ(long qq) {
        for (PersonInfo pi : personInfo) {
            if (pi.qq == qq) {
                return pi;
            }
        }
        return null;
    }

    public PersonInfo getPersonInfoFromName(String name) {
        for (PersonInfo pi : personInfo) {
            if (pi.name.equals(name)) {
                return pi;
            }
        }
        return null;
    }

    public PersonInfo getPersonInfoFromBid(long bid) {
        for (PersonInfo pi : personInfo) {
            if (pi.bid == bid) {
                return pi;
            }
        }
        return null;
    }

	public PersonInfo getPersonInfoFromLiveId(long lid) {
        for (PersonInfo pi : personInfo) {
            if (pi.bliveRoom == lid) {
                return pi;
			}
		}
        return null;
	}
}
