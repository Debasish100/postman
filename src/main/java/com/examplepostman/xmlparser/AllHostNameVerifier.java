package com.examplepostman.xmlparser;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class AllHostNameVerifier implements HostnameVerifier {

	public boolean verify(String arg0, SSLSession arg1) {
		// TODO Auto-generated method stub
		return true;
	}

}
