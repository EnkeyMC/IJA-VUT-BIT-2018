package ija.project.exception;

/**
 * Formula parsing exception
 */
public class ExpressionException extends RuntimeException {

	/**
	 * Construct object
	 * @param msg error message
	 */
	public ExpressionException(String msg) {
		super(msg);
	}
}
