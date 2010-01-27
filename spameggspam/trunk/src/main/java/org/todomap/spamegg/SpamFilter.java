package org.todomap.spamegg;

/**
 * Interface for spam filters.
 * 
 * @author kocka
 */
public interface SpamFilter {

	/**
	 * Check if the content is spam or not.
	 * 
	 * @param content
	 *            the content submitted.
	 * @return true or false based on implementation
	 */
	boolean isSpam(Content content);

	/**
	 * Send a 'ham' (false positive) notification to the spam engine for a
	 * content that has been identified as a spam by the engine.
	 * 
	 * @param content
	 *            the content
	 */
	void falsePositive(Content content);

	/**
	 * Send a 'spam' notification to the spam engine for a content that has been
	 * identified as non-spam by the engine.
	 * 
	 * @param content
	 *            the content
	 */
	void falseNegative(Content content);
}
