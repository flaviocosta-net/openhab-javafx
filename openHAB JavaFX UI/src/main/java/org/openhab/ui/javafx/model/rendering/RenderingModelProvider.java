package org.openhab.ui.javafx.model.rendering;

import java.net.URI;

import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.NavigableContainer;

/**
 * Interface that defines the methods to retrieve a rendering model based on a
 * URI.
 * 
 * @author Flavio Costa
 */
public interface RenderingModelProvider {

	/**
	 * Obtains a RenderingModel based on the desired return type.
	 * 
	 * @return Rendering model for a certain Container type.
	 */
	static RenderingModelProvider get() {
		// for now, just return a hard-coded concrete implementation
		return new ClasspathResourceRenderingModel();
	}

	/**
	 * Retrieves a rendering model instance for a given URI.
	 * 
	 * @param uri
	 *            Navigable container URI.
	 * @return
	 * @return Rendering model instance, or null if there is no container
	 *         retrievable with the given URI.
	 */
	NavigableContainer<?, Container<?, ?>> retrieve(URI uri);
}
