package org.todomap.spamegg;

/**
 * Checked exception thrown by the {@link SpamFilter} interface.
 * 
 * @author kocka
 * 
 */
public class SpamFilterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474176127426733513L;

	/**
	 * {@inheritDoc}
	 */
	public SpamFilterException() {
	}

	/**
	 * {@inheritDoc}
	 */
	public SpamFilterException(final String message) {
		super(message);
	}

	/**
	 * {@inheritDoc}
	 */
	public SpamFilterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * {@inheritDoc}
	 */
	public SpamFilterException(final Throwable cause) {
		super(cause);
	}

}
