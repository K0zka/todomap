package org.todomap.alertbox.notifiers;

import java.io.IOException;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.todomap.alertbox.Monitorable.StatusDescription;
import org.todomap.alertbox.Notifier;

import rath.msnm.MSNMessenger;
import rath.msnm.entity.MsnFriend;
import rath.msnm.event.MsnAdapter;
import rath.msnm.msg.MimeMessage;

@XmlRootElement(name="msn")
public class MsnNotifier implements Notifier {
	String[] targetAddress;
	String msnUser;
	String msnPasswd;
	@XmlElement(name="to")
	public String[] getTargetAddress() {
		return targetAddress;
	}
	public void setTargetAddress(String[] targetAddress) {
		this.targetAddress = targetAddress;
	}
	public String getMsnUser() {
		return msnUser;
	}
	@XmlAttribute(name="user")
	public void setMsnUser(String msnUser) {
		this.msnUser = msnUser;
	}
	@XmlAttribute(name="pwd")
	public String getMsnPasswd() {
		return msnPasswd;
	}
	public void setMsnPasswd(String msnPasswd) {
		this.msnPasswd = msnPasswd;
	}

	@Override
	public void notify(StatusDescription statusDescription) {
		final MSNMessenger msnMessenger = new MSNMessenger(msnUser, msnPasswd);
		msnMessenger.addMsnListener(new MsnAdapter(){

			@Override
			public void loginComplete(MsnFriend own) {
				System.err.print(own.getFormattedFriendlyName());
			}

			@Override
			public void loginError(String header) {
				System.err.print(header);
			}});
		msnMessenger.login();
		try {
			msnMessenger.setMyFriendlyName("AlertBox");
			msnMessenger.setMyStatus("Bad news.");
			for(String address : targetAddress) {
				MimeMessage mimeMessage = new MimeMessage();
				mimeMessage.setMessage(""+statusDescription.getStatus() + " "+statusDescription.getDescription());
				msnMessenger.sendMessage(address, mimeMessage);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			msnMessenger.logout();
		}
	}
}
