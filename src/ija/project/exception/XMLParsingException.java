package ija.project.exception;

/**
 * XMLParsingException indicating that an error occurred during XML parsing
 */
public class XMLParsingException extends ApplicationException {

	/**
	 * Construct object
	 * @param msg error message
	 */
	public XMLParsingException(String msg) {
		super(msg);
	}
}
