package com.meng.botconnect.bean;

import com.meng.botconnect.lib.*;
import java.util.*;
import java.util.concurrent.*;

public class SanaeConfigBean {
	public HashMap<Long,String> welcomeMap = new HashMap<>();
	public HashMap<Long,PersonConfig> personCfg=new HashMap<>();
	public HashSet<Long> botOff = new HashSet<>();
	public HashSet<Long> zanSet = new HashSet<>();
	public ArrayList<ReportBean> reportList=new ArrayList<>();
	public ArrayList<BugReportBean> bugReportList=new ArrayList<>();
	public HashMap<Long,Boolean> dicRegex = new HashMap<>();
	public ArrayList<MessageWait> delayMsg=new ArrayList<>();
	public ConcurrentHashMap<Integer,BiliUser> biliMaster = new ConcurrentHashMap<>();

	void addReport(long fromGroup, long fromQQ, String content) {
		ReportBean report=new ReportBean();
		report.t = System.currentTimeMillis();
		report.g = fromGroup;
		report.q = fromQQ;
		report.c = content;
		reportList.add(report);
	}

	void addBugReport(long fromGroup, long fromQQ, String content) {
		BugReportBean bugReport=new BugReportBean();
		bugReport.t = System.currentTimeMillis();
		bugReport.g = fromGroup;
		bugReport.q = fromQQ;
		bugReport.c = content;
		bugReportList.add(bugReport);
	}

	ReportBean removeReport() {
		if (reportList.size() == 0) {
			return null;
		}
		ReportBean rb= reportList.remove(0);
		//ConfigManager.instence.saveSanaeConfig();
		return rb;
	}

	void reportToLast() {
		if (reportList.size() == 0) {
			return;
		}
		reportList.add(reportList.remove(0));
		//ConfigManager.instence.saveSanaeConfig();
	}

	ReportBean getReport() {
		if (reportList.size() == 0) {
			return null;
		}
		return reportList.get(0);
	}

	BugReportBean removeBugReport() {
		if (bugReportList.size() == 0) {
			return null;
		}
		BugReportBean brb= bugReportList.remove(0);
		//ConfigManager.instence.saveSanaeConfig();
		return brb;
	}

	void bugReportToLast() {
		if (bugReportList.size() == 0) {
			return;
		}
		bugReportList.add(bugReportList.remove(0));
		//ConfigManager.instence.saveSanaeConfig();
	}

	BugReportBean getBugReport() {
		if (bugReportList.size() == 0) {
			return null;
		}
		return bugReportList.get(0);
	}

	public class ReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", Tools.CQ.getTime(t), g, q, c);
		}
	}

	public class BugReportBean {
		public long t;//time
		public long g;//group
		public long q;//qq
		public String c;//content

		@Override
		public String toString() {
			return String.format("时间:%s,群:%d,用户:%d\n内容:%s", Tools.CQ.getTime(t), g, q, c);
		}
	}
}


