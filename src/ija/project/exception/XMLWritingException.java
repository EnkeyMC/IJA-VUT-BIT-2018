package ija.project.exception;

/**
 * XMLWritingException indicating that an error occurred during XML writing
 */
public class XMLWritingException extends ApplicationException {
	/**
	 * Construct object
	 *
	 * @param msg error message
	 */
	public XMLWritingException(String msg) {
		super(msg);
	}
}
