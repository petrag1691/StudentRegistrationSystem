/**
 * This is a bean class containing basic error attributes including: Error Code, Message
 * @author Raghu Keerthi Petla, Aditi Vishwas Rao, Mehul Parmar, Anirudh Palakurthi 
 */
package com.uic.ids520.bean;

public class ErrorMessageBean {
	private int errorCode;
	private String errorMessage;
	private String sqlState;
	public ErrorMessageBean(){
		errorCode =0;
		errorMessage = "";
		setSqlState("");
	}
	public int getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public String getSqlState() {
		return sqlState;
	}
	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}
	
	public void setErrorMessage(int errorCode,String sqlState,String errorMessage){
		if(!sqlState.isEmpty()){
			this.sqlState = sqlState;
		}else{
			this.sqlState = "";
		}
		if(errorCode!=-1){
			this.errorCode = errorCode;
		}else{
			this.errorCode = -1;
		}
		this.errorMessage = errorMessage;
	}
}
