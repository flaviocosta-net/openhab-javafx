package org.openhab.ui.javafx.scheme;

import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Defines the interface for any implementing class that is able to handle
 * certain URI scheme types.
 * 
 * TODO For now, the concept of this interface is very provisional, but it's
 * based on the idea of using registry-based URIs instead of URLs to reference
 * certain resources. Eventually it should allow some functionality not
 * supported today (e.g. one sitemap linking to another), but for now it just
 * serves to simulate sitemap navigation using local resources only. It should
 * be reformulated once we have actual integration with RESTful APIs, and some
 * of the ideas implemented here will likely be moved to the server-side, so we
 * can keep client implementations as simple as possible.
 * 
 * @author Flavio Costa
 */
public interface SchemeHandler<T> {

	/**
	 * Registry of handler instances indexed by scheme type.
	 */
	static final Map<SchemeType, SchemeHandler<?>> handlersByType = new HashMap<>();

	/**
	 * Obtains a scheme handler for a given type. The calling application is
	 * expected to know the return type for each SchemaHandler implementation (e.g.
	 * a handler for icons should always return a {@link InputStream} with the icon
	 * contents).
	 * 
	 * @param inputType
	 *            Input type supported by the handler.
	 * @return Handler instance, if there is a matching one in the registry.
	 * 
	 * @throws IllegalArgumentException
	 *             If no matching handler is registered or if the return type is
	 *             wrong.
	 */
	@SuppressWarnings("unchecked")
	@NonNull
	static <R> SchemeHandler<R> of(SchemeType inputType) {
		SchemeHandler<?> handler = handlersByType.get(inputType);
		if (handler == null) {
			throw new IllegalArgumentException("No handler registered for input type " + inputType);
		}
		try {
			// by suppressing the cast check warning here, we essentially delegate to the
			// calling class the decision/knowledge of what is the return type for the
			// desired SchemeHandler implementation
			return (SchemeHandler<R>) handler;
		} catch (ClassCastException e) {
			// if the programmer got the return type wrong,
			// at least explain it with a clearer message
			throw new IllegalArgumentException(
					"The handler for " + inputType + " does not implement the expected return type");
		}
	}

	/**
	 * Registers a handler for all types accepted by it. Any pre-existing mappings
	 * for the types will be replaced.
	 * 
	 * @param handler
	 *            Handler instance to be registered.
	 */
	static void register(SchemeHandler<?> handler) {
		// maps the handler having all its accepted types as the keys
		handlersByType.putAll(handler.getAcceptedTypes().stream().collect(Collectors.toMap(t -> t, t -> handler)));
	}

	/**
	 * Returns the set of SchemeTypes accepted by the implementing class.
	 * 
	 * @return Non-empty set.
	 */
	Set<SchemeType> getAcceptedTypes();

	/**
	 * Determines whether this SchemeHandler is able to handle a certain scheme.
	 * 
	 * @param scheme
	 *            Scheme to check.
	 * @return True if this handler can process the specific scheme.
	 */
	default boolean accepts(String scheme) {
		return getAcceptedTypes().stream().anyMatch(t -> t.includes(scheme));
	}

	/**
	 * Returns the latest or current URI associated with this SchemeHandler.
	 * Implementing classes may provide specific logic for this method, which may be
	 * the last URI successfully processed by the method {@link #validate(URI)}, a
	 * URI set on the {@link #handle(URI)} method or something else.
	 * 
	 * @return URI currently associated with this SchemeHandler.
	 */
	default URI getURI() {
		throw new UnsupportedOperationException("getURI() method not implemented on " + getClass());
	}

	/**
	 * Validates a URI to determine whether it is supported by the handler
	 * implementation or not. This method may be overwritten to implement different
	 * validation criteria.
	 * 
	 * @param uri
	 *            URI to be validated.
	 * @throws IllegalArgumentException
	 *             If the URI is null or not supported by the implementing class.
	 */
	default void validate(URI uri) throws IllegalArgumentException {
		if (uri == null || !accepts(uri.getScheme())) {
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	/**
	 * Convenience method that calls {@link #handle(URI)} after converting the input
	 * parameter into a URI valid for this SchemeHandler.
	 * 
	 * @param input
	 *            Input String to be converted to URI.
	 * @return Result of the execution.
	 * @throws IllegalArgumentException
	 *             If the resulting URI is not valid or not supported by the
	 *             implementing class.
	 */
	default T handle(String input) throws IllegalArgumentException {
		URI uri = parseInput(input);
		if (!uri.isOpaque() && uri.getPath().isEmpty()) {
			// by default, adds a trailing slash to empty paths to indicate
			// the root resource when the URI is hierarchical
			uri = uri.resolve("/");
		}
		return handle(uri);
	}

	/**
	 * Converts the input parameter into a URI to be handled by this SchemeHandler.
	 * Implementing classes may define specific logic to transform an String into an
	 * URI without having to deal with URi creation externally.
	 * 
	 * @param input
	 *            Input String to be converted to URI.
	 * @return Resulting URI.
	 */
	default URI parseInput(String input) {
		return URI.create(input);
	}

	/**
	 * Processes an input URI. Implementations should normally call
	 * {@link #validate(URI)} before processing the URI to make sure it is supported
	 * by the handler.
	 * 
	 * @param uri
	 *            URI to be acted upon.
	 * @return Result of the execution.
	 * @throws IllegalArgumentException
	 *             If the URI is not valid or not supported by the implementing
	 *             class.
	 */
	T handle(URI uri) throws IllegalArgumentException;
}
