package ija.project.schema;

import java.security.KeyException;

public interface OutputPort {
	/**
	 * Set port value
	 * @param key value key
	 * @param value value to set
	 */
	void setValue(String key, Double value) throws KeyException;
}
