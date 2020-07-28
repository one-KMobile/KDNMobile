package com.kdn.mtps.mobile.info;

import java.util.List;

import com.kdn.mtps.mobile.notice.NoticeInfo;

public class GData {
	static List<NoticeInfo> noticeList;// 로그인 후 Hong 서버로부터 받는 데이터

	public static void clearData() {
		noticeList = null;
	}

	public static List<NoticeInfo> getNoticeList() {
		return noticeList;
	}
	
	public static void setNoticeList(List<NoticeInfo> list) {
		noticeList = list;
	}
	
}
