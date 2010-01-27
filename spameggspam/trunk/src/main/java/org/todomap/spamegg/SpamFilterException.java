package org.todomap.spamegg;

public class SpamFilterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6474176127426733513L;

	public SpamFilterException() {
	}

	public SpamFilterException(String message) {
		super(message);
	}

	public SpamFilterException(Throwable cause) {
		super(cause);
	}

	public SpamFilterException(String message, Throwable cause) {
		super(message, cause);
	}

}
