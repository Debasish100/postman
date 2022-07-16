package com.examplepostman.form;

public class RequestXmlForm {
	
	/*
	 * public String id; public String type; public String description;
	 */
	public String eSignRequest;
	public String aspTxnID;
	public String contentType;
	/*
	 * public String getId() { return id; } public void setId(String id) { this.id =
	 * id; } public String getType() { return type; } public void setType(String
	 * type) { this.type = type; } public String getDescription() { return
	 * description; } public void setDescription(String description) {
	 * this.description = description;
	 * }
	 */
	
	@Override
	public String toString() {
		return "RequestXmlForm [eSignRequest=" + eSignRequest + ", aspTxnID=" + aspTxnID + ", contentType="
				+ contentType + "]";
	}
	public String geteSignRequest() {
		return eSignRequest;
	}
	public void seteSignRequest(String eSignRequest) {
		this.eSignRequest = eSignRequest;
	}
	public String getAspTxnID() {
		return aspTxnID;
	}
	public void setAspTxnID(String aspTxnID) {
		this.aspTxnID = aspTxnID;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	
	

}
