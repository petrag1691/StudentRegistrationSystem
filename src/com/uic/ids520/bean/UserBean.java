package com.uic.ids520.bean;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

public class UserBean implements HttpSessionBindingListener{
	private String firstName;
	private String lastName;
	private String userId;
	private String major;
	private String password;
	private boolean alreadyLoggedIn;
	private String ipaddress;
	private static Map<UserBean, HttpSession> logins = new ConcurrentHashMap<>();
	public static String oldip = "";
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getMajor() {
		return major;
	}
	public void setMajor(String major) {
		this.major = major;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAlreadyLoggedIn() {
		return alreadyLoggedIn;
	}
	public void setAlreadyLoggedIn(boolean alreadyLoggedIn) {
		this.alreadyLoggedIn = alreadyLoggedIn;
	}
	
	public String getIpaddress() {
		return ipaddress;
	}
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	@Override
	  public boolean equals(Object other) {
	    return (other instanceof UserBean) && (userId != null) ? 
	     userId.equals(((UserBean) other).getUserId()) : (other == this);
	  }

	  @Override
	  public int hashCode() {
	    return (this.getUserId() != null) ? 
	     (this.getClass().hashCode() + this.getUserId().hashCode()) : super.hashCode();
	  }
	  
	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		// TODO Auto-generated method stub
		HttpSession oldSession = logins.get(this);
		System.out.println("UserBean.valueBound() logins size is "+logins.size());
	    if (oldSession != null) {
	    	UserBean existingUser = (UserBean)oldSession.getAttribute("userBean");
	    	oldip = existingUser.getIpaddress();
	    	alreadyLoggedIn = true;
	    } else {
	    	alreadyLoggedIn = false;
	      logins.put(this, event.getSession());
	      oldip = "";
	    }

		System.out.println("Value Bound Called, " + event.getValue() + " isNewSession: " + event.getSession().isNew());
	}
	@Override
	public void valueUnbound(HttpSessionBindingEvent arg0) {
		// TODO Auto-generated method stub
		logins.remove(this);
		alreadyLoggedIn = false;
		oldip = "";
		System.out.println("UserBean.valueUnbound() logins size is "+logins.size());
		System.out.println("Value UnBound Called, " + arg0.getValue() + " isNewSession: " + arg0.getSession().isNew());
	}
	
}
