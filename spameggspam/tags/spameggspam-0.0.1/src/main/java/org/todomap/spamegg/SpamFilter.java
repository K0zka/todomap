package org.todomap.spamegg;

/**
 * Interface for spam filters.
 * 
 * @author kocka
 */
public interface SpamFilter {

	/**
	 * Send a 'spam' notification to the spam engine for a content that has been
	 * identified as non-spam by the engine.
	 * 
	 * @param content
	 *            the content
	 * @throws SpamFilterException
	 *             if there is an error accessing the spam filter engine
	 */
	void falseNegative(Content content) throws SpamFilterException;

	/**
	 * Send a 'ham' (false positive) notification to the spam engine for a
	 * content that has been identified as a spam by the engine.
	 * 
	 * @param content
	 *            the content
	 * @throws SpamFilterException
	 *             if there is an error accessing the spam filter engine
	 */
	void falsePositive(Content content) throws SpamFilterException;

	/**
	 * Check if the content is spam or not.
	 * 
	 * @param content
	 *            the content submitted.
	 * @return true or false based on implementation
	 * @throws SpamFilterException
	 *             if there is an error accessing the spam filter engine
	 */
	boolean isSpam(Content content) throws SpamFilterException;
}
