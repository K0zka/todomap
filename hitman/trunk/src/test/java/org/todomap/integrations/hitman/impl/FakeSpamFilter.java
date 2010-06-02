package org.todomap.integrations.hitman.impl;

import org.todomap.spamegg.Content;
import org.todomap.spamegg.SpamFilter;
import org.todomap.spamegg.SpamFilterException;

public class FakeSpamFilter implements SpamFilter {

	boolean spam = false;

	@Override
	public void falseNegative(Content arg0) throws SpamFilterException {
	}

	@Override
	public void falsePositive(Content arg0) throws SpamFilterException {
	}

	@Override
	public boolean isSpam(Content arg0) throws SpamFilterException {
		return spam;
	}

}
