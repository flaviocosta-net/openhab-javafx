package org.openhab.ui.javafx.model;

import org.openhab.ui.javafx.model.atom.ActionableAtom;
import org.openhab.ui.javafx.model.atom.IconAtom;
import org.openhab.ui.javafx.model.atom.MappingsAtom;
import org.openhab.ui.javafx.model.atom.SliderAtom;
import org.openhab.ui.javafx.model.atom.TextAtom;
import org.openhab.ui.javafx.model.container.Frame;
import org.openhab.ui.javafx.model.container.Page;
import org.openhab.ui.javafx.model.container.Sitemap;
import org.openhab.ui.javafx.model.container.Widget;

/**
 * Enumerates all supported Component types, mapping each one of them to the
 * class that implements it on the Java model.
 * 
 * @author Flavio Costa
 */
public enum ComponentType {

	SMARTHOME(Sitemap.class), FRAME(Frame.class), GROUP(ActionableAtom.class), ICON(IconAtom.class), PAGE(
			Page.class), LABEL(TextAtom.class), SELECTION(MappingsAtom.class), SLIDER(
					SliderAtom.class), SWITCH(MappingsAtom.class), TEXT(TextAtom.class), WIDGET(Widget.class);

	/**
	 * Class implementing this component type.
	 */
	private Class<? extends Component<?>> implementingClass;

	/**
	 * Constructor that defines the implementing class.
	 * 
	 * @param ic
	 *            Implementing class reference.
	 */
	ComponentType(Class<? extends Component<?>> ic) {
		this.implementingClass = ic;
	}

	/**
	 * Returns the implementing class.
	 * 
	 * @return Class that implements this component type in the model.
	 */
	public Class<? extends Component<?>> getImplementingClass() {
		return implementingClass;
	}
}