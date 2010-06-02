package org.todomap.integrations.hitman.impl;

import org.todomap.integrations.hitman.Filter;
import org.todomap.spamegg.Content;
import org.todomap.spamegg.SpamFilter;
import org.todomap.spamegg.SpamFilterException;
import org.todomap.spamegg.User;
import org.w3c.dom.Document;

public class SpamFilterAdapter implements Filter {

	ResponseService responseService;
	public ResponseService getResponseService() {
		return responseService;
	}

	public void setResponseService(ResponseService responseService) {
		this.responseService = responseService;
	}

	SpamFilter filter;

	public SpamFilter getFilter() {
		return filter;
	}

	public void setFilter(SpamFilter filter) {
		this.filter = filter;
	}

	public String filter(String message) {
		try {
			final Document document = Utils.parse(message);
			final User author = new User(Utils.getXpathValue(
					"/invocation/user/displayName/text()", document), "", Utils
					.getXpathValue("/invocation/user/email/text()", document),
					"http://hu.todomap.org/"
							+ Utils.getXpathValue("/invocation/user/id/text()",
									document) + ".html", "");
			final Content content = new Content(author, "", "", "comment",
					Utils.getXpathValue(
							"/invocation/result/description/text()", document));
			if (filter.isSpam(content)) {
				responseService.send(new IntegrationNotificationMessage(
						"org.todomap.spamfilter", "spam", "Content is spam",
						Long.parseLong(Utils.getXpathValue(
								"/invocation/result/id/text()", document))));
				return null;
			} else {
				return message;
			}
		} catch (SpamFilterException e) {
			throw new RuntimeException(e);
		}
	}

}
