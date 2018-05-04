package org.openhab.ui.javafx.model.rendering;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openhab.ui.javafx.model.Component;
import org.openhab.ui.javafx.model.ComponentType;
import org.openhab.ui.javafx.model.container.Container;
import org.openhab.ui.javafx.model.container.NavigableContainer;
import org.openhab.ui.javafx.model.container.Page;
import org.openhab.ui.javafx.model.container.Sitemap;

import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

/**
 * Deserializes the rendering model from a local classpath resource.
 * 
 * @author Flavio Costa
 */
public class ClasspathResourceRenderingModel implements RenderingModelProvider {

	/**
	 * Used for logging activities of this class.
	 */
	private static final Logger logger = Logger.getLogger(ClasspathResourceRenderingModel.class.getPackage().getName());

	@Override
	public NavigableContainer<?, Container<?, ?>> retrieve(URI uri) {

		GsonBuilder gsonBuilder = new GsonBuilder();
		ComponentDeserializer componentDeserializer = new ComponentDeserializer();
		gsonBuilder.registerTypeAdapter(Component.class, componentDeserializer);
		gsonBuilder.registerTypeAdapter(Container.class, componentDeserializer);
		gsonBuilder.registerTypeAdapter(ComponentType.class, new ComponentTypeDeserializer());

		String scheme = uri.getScheme();
		String id = uri.getHost();
		// here the trailing slash should become the file extension
		String path = uri.getPath().replaceAll("/$", "\\.json");
		try (InputStream is = Component.class.getResourceAsStream(String.format("%s/%s%s", scheme, id, path))) {

			if (is == null) {
				logger.log(Level.SEVERE, String.format("Resource not found for %s", uri));
			} else {
				try (JsonReader reader = new JsonReader(new InputStreamReader(is))) {
					return gsonBuilder.create().fromJson(reader, getContainerClass(path));
				}
			}

		} catch (IOException e) {
			logger.log(Level.SEVERE, String.format("Loading %s failed", uri), e);
		}

		return null;
	}

	/**
	 * Determines at runtime which class to use for deserialization.
	 * 
	 * @param path
	 *            URI path.
	 * @return Type of the Container implementation.
	 */
	private Type getContainerClass(String path) {
		// empty or root path? => Sitemap, else Page
		return path.equals(".json") ? Sitemap.class : Page.class;
	}
}
