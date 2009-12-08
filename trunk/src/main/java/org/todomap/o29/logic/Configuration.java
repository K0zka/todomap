package org.todomap.o29.logic;

public class Configuration {
	boolean debugEnabled = false;
	String googleWebmastersVerification = "";
	String yahooSiteExplorerVerification = "";

	public String getGoogleWebmastersVerification() {
		return googleWebmastersVerification;
	}

	public void setGoogleWebmastersVerification(String googleWebmastersVerification) {
		this.googleWebmastersVerification = googleWebmastersVerification;
	}

	public String getYahooSiteExplorerVerification() {
		return yahooSiteExplorerVerification;
	}

	public void setYahooSiteExplorerVerification(
			String yahooSiteExplorerVerification) {
		this.yahooSiteExplorerVerification = yahooSiteExplorerVerification;
	}

	public boolean isDebugEnabled() {
		return debugEnabled;
	}

	public void setDebugEnabled(boolean debugEnabled) {
		this.debugEnabled = debugEnabled;
	}
	
}
