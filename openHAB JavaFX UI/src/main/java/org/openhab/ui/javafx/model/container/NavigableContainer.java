package org.openhab.ui.javafx.model.container;

import java.net.URI;

import org.openhab.ui.javafx.model.container.NavigableContainer.Data;

/**
 * Defines a Container subtype which is navigable, meaning that it contains the
 * methods (actually the specific Data definition) with specific information
 * that may be displayed when the user "navigates" into it (e.g. a label).
 * 
 * @author Flavio Costa
 *
 * @param <D>
 *            Type of the data field.
 * @param <C>
 *            Type of each component contained in this container.
 */
public interface NavigableContainer<D extends Data, C extends Container<?, ?>> extends Container<D, C> {

	/**
	 * Returns the data defined for navigable containers.
	 */
	public interface Data {

		/**
		 * Returns the URI for the container.
		 * 
		 * @return Container URI.
		 */
		URI getURI();

		/**
		 * Returns the label for the container.
		 * 
		 * @return Display label.
		 */
		String getLabel();

		/**
		 * Returns the icon for the container.
		 * 
		 * @return Icon URI.
		 */
		URI getIcon();
	}

	/**
	 * Returns the data for the container.
	 * 
	 * @return Data associated with this container.
	 */
	D getData();
}
