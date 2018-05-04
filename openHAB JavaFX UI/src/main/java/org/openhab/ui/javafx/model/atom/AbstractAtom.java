package org.openhab.ui.javafx.model.atom;

import org.openhab.ui.javafx.model.AbstractComponent;

/**
 * Abstract Atom implementation. It provides a standard implementation for Atom
 * subclasses.
 * 
 * @author Flavio Costa`
 * 
 * @param <D>
 *            Type of the data field.
 */
public abstract class AbstractAtom<D> extends AbstractComponent<D> implements Atom<D> {

	/**
	 * Data associated to this atom.
	 */
	private D data;

	@Override
	public D getData() {
		return data;
	}
}
