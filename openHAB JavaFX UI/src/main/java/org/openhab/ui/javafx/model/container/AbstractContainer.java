package org.openhab.ui.javafx.model.container;

import java.util.List;

import org.openhab.ui.javafx.model.AbstractComponent;
import org.openhab.ui.javafx.model.Component;

/**
 * Abstract Container implementation. It provides a standard implementation for
 * Container subclasses.
 * 
 * @author Flavio Costa
 * 
 * @param <D>
 *            Type of the data field.
 * @param <C>
 *            Type of each component contained in this container.
 */
public abstract class AbstractContainer<D, C extends Component<?>> extends AbstractComponent<D>
		implements Container<D, C> {

	/**
	 * Components in the Container.
	 */
	protected List<C> components;

	@Override
	public List<C> getComponents() {
		return components;
	}
}
