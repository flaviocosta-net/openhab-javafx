package org.openhab.ui.javafx.view.iconset;

import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import org.openhab.ui.javafx.scheme.SchemeHandler;
import org.openhab.ui.javafx.scheme.SchemeType;

/**
 * Loads icons from the available icon sets.
 * 
 * The URI should follow the format icon:[<iconset>/]category[#state] 
 * 
 * @author Flavio Costa
 */
public class IconSchemeHandler implements SchemeHandler<Optional<InputStream>> {

	/**
	 * Icon set used if none is specified in the URI.
	 */
	private static final String DEFAULT_ICONSET = "classic";

	@Override
	public Set<SchemeType> getAcceptedTypes() {
		return Collections.singleton(SchemeType.ICON);
	}

	@Override
	public Optional<InputStream> handle(URI uri) throws IllegalArgumentException {
		validate(uri);

		String path = uri.getSchemeSpecificPart();

		long numberofSlashes = path.codePoints().filter(ch -> ch == '/').count();
		switch ((int) numberofSlashes) {
		case 0:
			path = DEFAULT_ICONSET + '/' + path;
			break;
		case 1:
			// no processing needed
			break;
		default:
			throw new IllegalArgumentException(uri + " cannot contain more than one slash");
		}
		
		InputStream is = null;
		
		String fragment = uri.getFragment();
		if (fragment != null) {
			// tries to retrieve the icon with a state
			is = getClass().getResourceAsStream(path + '-' + fragment.toLowerCase() + ".png");
		}

		// not found or no state defined, load the standard category icon
		if(is == null) {
			is = getClass().getResourceAsStream(path + ".png");
		}
		return Optional.ofNullable(is);
	}
}
