package org.openhab.ui.javafx.model;

/**
 * Defines common methods for all Components. This is the most general entity
 * contained in a sitemap.
 * 
 * @author Flavio Costa
 * 
 * @param <D>
 *            Type of the data field.
 */
public interface Component<D> {

	/**
	 * Returns the layout for the component.
	 * 
	 * @return Component layout.
	 */
	String getLayout();

	/**
	 * Returns the style for the component.
	 * 
	 * @return Component layout.
	 */
	String getStyle();

	/**
	 * Returns the defined type for the component.
	 * 
	 * @return Component type.
	 */
	ComponentType getType();
	
	/**
	 * Returns the data defined for the component.
	 * 
	 * @return Component data.
	 */
	D getData();
}
