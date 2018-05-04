package org.openhab.ui.javafx.model.container;

import java.net.URI;

import org.openhab.ui.javafx.model.atom.ActionableAtom;
import org.openhab.ui.javafx.model.atom.Atom;

/**
 * Widget model implementation. A widget only contains atoms, but not other
 * containers. Its action is determined by the first {@link ActionableAtom} it
 * contains.
 * 
 * @author Flavio Costa
 */
public class Widget extends AbstractContainer<URI, Atom<?>> {

	@Override
	public URI getData() {
		// URI of the first component that is an Actionable Atom
		return components.stream().filter(ActionableAtom.class::isInstance).findFirst().map(ActionableAtom.class::cast)
				.map(ActionableAtom::getData).orElse(null);
	}
}
