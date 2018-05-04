package org.openhab.ui.javafx.model.container;

import java.util.List;

import org.openhab.ui.javafx.model.Component;

/**
 * Defines common methods for all Containers. A Container must contain a
 * non-empty list of components.
 * 
 * @author Flavio Costa
 *
 * @param <D>
 *            Type of the data field.
 * @param <C>
 *            Type of each component contained in this container.
 */
public interface Container<D, C extends Component<?>> extends Component<D> {

	/**
	 * Returns the components in the container.
	 * 
	 * @return List of components in this container.
	 */
	public List<C> getComponents();
}
