package com.exception;

public class errordtls {

	private String errorCd;

	private String errormsg;

	private String errorattribute;

	public errordtls(String errorCd, String errormsg, String errorattribute) {
		super();
		this.errorCd = errorCd;
		this.errormsg = errormsg;
		this.errorattribute = errorattribute;
	}

	public String getErrorCd() {
		return errorCd;
	}

	public void setErrorCd(String errorCd) {
		this.errorCd = errorCd;
	}

	public String getErrormsg() {
		return errormsg;
	}

	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}

	public String getErrorattribute() {
		return errorattribute;
	}

	public void setErrorattribute(String errorattribute) {
		this.errorattribute = errorattribute;
	}

}
