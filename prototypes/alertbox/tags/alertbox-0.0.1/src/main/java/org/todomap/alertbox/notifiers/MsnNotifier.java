package org.todomap.alertbox.notifiers;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.jml.Email;
import net.sf.jml.MsnMessenger;
import net.sf.jml.event.MsnAdapter;
import net.sf.jml.impl.MsnMessengerFactory;
import net.sf.jml.message.MsnSystemMessage;

import org.todomap.alertbox.Monitorable.StatusDescription;
import org.todomap.alertbox.Monitorable;
import org.todomap.alertbox.Notifier;

@XmlRootElement(name = "msn")
public class MsnNotifier implements Notifier {
	String[] targetAddress;
	String msnUser;
	String msnPasswd;

	@XmlElement(name = "to")
	public String[] getTargetAddress() {
		return targetAddress;
	}

	public void setTargetAddress(String[] targetAddress) {
		this.targetAddress = targetAddress;
	}

	public String getMsnUser() {
		return msnUser;
	}

	@XmlAttribute(name = "user")
	public void setMsnUser(String msnUser) {
		this.msnUser = msnUser;
	}

	@XmlAttribute(name = "pwd")
	public String getMsnPasswd() {
		return msnPasswd;
	}

	public void setMsnPasswd(String msnPasswd) {
		this.msnPasswd = msnPasswd;
	}

	@Override
	public void notify(Monitorable monitorable, StatusDescription statusDescription) {
		if (loggedin) {
			for (final String addr : targetAddress) {
				messenger.sendText(
						Email.parseStr(addr),
						statusDescription.getStatus() + " "
								+ statusDescription.getDescription());
			}
		} else {
			messageQueue.add(statusDescription);
		}
	}

	MsnMessenger messenger;
	boolean loggedin = false;
	Queue<StatusDescription> messageQueue = new ArrayBlockingQueue<StatusDescription>(1024);

	@Override
	public void start() {
		messenger = MsnMessengerFactory.createMsnMessenger(msnUser, msnPasswd);
		messenger.setLogIncoming(true);
		messenger.setLogOutgoing(true);
		messenger.addListener(new MsnAdapter(){

			@Override
			public void ownerStatusChanged(MsnMessenger messenger) {
				System.out.println(messenger.getOwner().getStatus());
			}

			@Override
			public void systemMessageReceived(MsnMessenger messenger,
					MsnSystemMessage message) {
				System.out.println(message.getContent());
			}

			@Override
			public void loginCompleted(MsnMessenger messenger) {
				System.out.println("logged on");
			}

			@Override
			public void contactListInitCompleted(MsnMessenger messenger) {
				loggedin = true;
				for(String addr : targetAddress) {
					messenger.sendText(Email.parseStr(addr), "Hello!");
				}
			}});
		messenger.login();

	}
}
