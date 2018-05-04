package org.openhab.ui.javafx.model.container;

import java.net.URI;

/**
 * Abstract Navigable Container implementation. It provides a standard
 * implementation for NavigableContainer subclasses.
 * 
 * @author Flavio Costa
 * 
 * @param <D>
 *            Type of the data field.
 * @param <C>
 *            Type of each component contained in this container. Navigable
 *            containers can only contain other containers, but not atoms.
 */
public abstract class AbstractNavigableContainer<D extends NavigableContainer.Data, C extends Container<?, ?>>
		extends AbstractContainer<D, C> implements NavigableContainer<D, C> {

	/**
	 * Data for this navigable container.
	 */
	private D data;

	/**
	 * Implementation of the Data interface for navigable containers. This is made a
	 * static class to avoid an "infinite recursion" on Java generics in subclass
	 * definitions.
	 */
	public static class Data implements NavigableContainer.Data {

		/**
		 * URI of the navigable container.
		 */
		private URI uri;

		/**
		 * Label for the container.
		 */
		private String label;

		/**
		 * Icon for the container.
		 */
		private URI icon;

		@Override
		public URI getURI() {
			return uri;
		}

		@Override
		public String getLabel() {
			return label;
		}

		@Override
		public URI getIcon() {
			return icon;
		}
	}

	@Override
	public D getData() {
		return data;
	}
}
