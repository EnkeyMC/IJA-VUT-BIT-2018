package ija.project.exception;

/**
 * Generic application exception
 */
public class ApplicationException extends RuntimeException {

	/**
	 * Construct object
	 * @param msg error message
	 */
	public ApplicationException(String msg) {
		super(msg);
	}
}
