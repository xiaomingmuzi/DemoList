package com.lixm.chat.bean;

import java.io.Serializable;

public class UserInfo implements Serializable {

	private String userId;
	private String nickName;
	private String isHead;
	private String picUrl;//用户头像
	private String userAccount;// 账号
	private String tempMinAttentionNews;
	private String issuePrimaryId;
	private String golden;// 金币数
	private String checkcode;//后踢前码
	private String password;// 用户账户的密码
	private String mobile;//手机号
	private String kt;// 博客界面请求是否开通博客参数
	private String memberId;// 用户的memberId
	private String token;//融云Token值

	
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getKt() {
		return kt;
	}

	public void setKt(String kt) {
		this.kt = kt;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCheckcode() {
		return checkcode;
	}

	public void setCheckcode(String checkcode) {
		this.checkcode = checkcode;
	}

	public String getGolden() {
		return golden;
	}

	public void setGolden(String golden) {
		this.golden = golden;
	}

	public String getIssuePrimaryId() {
		return issuePrimaryId;
	}

	public void setIssuePrimaryId(String issuePrimaryId) {
		this.issuePrimaryId = issuePrimaryId;
	}


	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String getTempMinAttentionNews() {
		return tempMinAttentionNews;
	}

	public void setTempMinAttentionNews(String tempMinAttentionNews) {
		this.tempMinAttentionNews = tempMinAttentionNews;
	}


	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @return the userAccount
	 */
	public String getUserAccount() {
		return userAccount;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getIsHead() {
		return isHead;
	}

	public void setIsHead(String isHead) {
		this.isHead = isHead;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	@Override
	public String toString() {
		return "UserInfo [isHead=" + isHead + ", picUrl=" + picUrl + ", userAccount=" + userAccount
				+ ", tempMinAttentionNews=" + tempMinAttentionNews + ", issuePrimaryId=" + issuePrimaryId + ", golden="
				+ golden + ", checkcode=" + checkcode + "]";
	}

}
