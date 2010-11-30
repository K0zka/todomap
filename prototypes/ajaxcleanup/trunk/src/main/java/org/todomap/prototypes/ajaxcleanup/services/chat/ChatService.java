package org.todomap.prototypes.ajaxcleanup.services.chat;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.todomap.prototypes.ajaxcleanup.channel.Messenger;
import org.todomap.prototypes.ajaxcleanup.messages.AddMessage;

@Path("/chat/")
public class ChatService {
	public ChatService(Messenger messenger) {
		super();
		this.messenger = messenger;
	}

	final Messenger messenger;

	@POST
	@Path("/send")
	public void message(@FormParam("from") String from,
			@FormParam("room") String room, @FormParam("message") String message) {
		messenger.message(new AddMessage());
	}

}
