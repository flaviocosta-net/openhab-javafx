package org.openhab.ui.javafx.model;

/**
 * Abstract Component implementation. It provides a standard implementation for
 * Component subclasses.
 * 
 * @author Flavio Costa
 * 
 * @param <D>
 *            Type of the data field.
 */
public abstract class AbstractComponent<D> implements Component<D> {

	/**
	 * Component type.
	 */
	private ComponentType type;

	/**
	 * Component layout.
	 */
	private String layout;

	/**
	 * Component style.
	 */
	private String style;

	@Override
	public ComponentType getType() {
		return type;
	}

	@Override
	public String getLayout() {
		return layout;
	}

	@Override
	public String getStyle() {
		return style;
	}
}
