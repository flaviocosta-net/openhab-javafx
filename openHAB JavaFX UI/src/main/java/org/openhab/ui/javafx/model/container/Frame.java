package org.openhab.ui.javafx.model.container;

/**
 * Frame model implementation.
 * 
 * @author Flavio Costa
 */
public class Frame extends AbstractContainer<String, Container<?, ?>> {

	/**
	 * Data for the Frame.
	 */
	private String data; 

	/**
	 * Returns the data defined for the frame.
	 * 
	 * @return Frame data.
	 */
	public String getData() {
		return data;
	}
}
