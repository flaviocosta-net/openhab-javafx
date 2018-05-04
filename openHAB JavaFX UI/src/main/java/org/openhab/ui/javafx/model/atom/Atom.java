package org.openhab.ui.javafx.model.atom;

import org.openhab.ui.javafx.model.Component;

/**
 * Defines common methods for all Atoms. An Atom must be an indivisible
 * component, which cannot contain any other elements, other than a single data
 * attribute.
 * 
 * @author Flavio Costa
 *
 * @param <D>
 *            Type of the data field.
 */
public interface Atom<D> extends Component<D> {

	// for now, an Atom is just a specialized component
}
